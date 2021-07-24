package framework.web.processor.impl;

import framework.core.BeanContainer;
import framework.web.annotation.Controller;
import framework.util.ConversionUtil;
import framework.util.ValidationUtil;
import framework.web.RequestProcessorChain;
import framework.web.annotation.*;
import framework.web.constant.MediaType;
import framework.web.constant.StatusCode;
import framework.web.exception.WebException;
import framework.web.processor.RequestProcessor;
import framework.web.renderer.ResultRenderer;
import framework.web.renderer.impl.*;
import framework.web.serializer.BodySerializer;
import framework.web.serializer.impl.DefaultBodySerializer;
import framework.web.type.ControllerMethod;
import framework.web.type.RequestPathInfo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static framework.web.WebApplication.DISPATCHER_SERVLET;

@Slf4j
public class ControllerRequestProcessor implements RequestProcessor {
    private final BeanContainer beanContainer = BeanContainer.getInstance();
    private final Map<RequestPathInfo, ControllerMethod> pathToMethodMap = new ConcurrentHashMap<>();

    public ControllerRequestProcessor() {
        log.info("Using servlet \"{}\" for Controller", DISPATCHER_SERVLET);

        Set<Class<?>> controllerClassSet = beanContainer.getClassesByAnnotation(Controller.class);
        Set<Class<?>> requestMappingClassSet = beanContainer.getClassesByAnnotation(RequestMapping.class);
        Set<Class<?>> classSet = new HashSet<>();
        if (!ValidationUtil.isEmpty(controllerClassSet)) {
            classSet.addAll(controllerClassSet);
        }
        if (!ValidationUtil.isEmpty(requestMappingClassSet)) {
            classSet.addAll(requestMappingClassSet);
        }
        buildPathToMethodMap(classSet);
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Throwable {
        log.debug("Processing request: {} {}",
                requestProcessorChain.getRequestMethod(),
                requestProcessorChain.getRequestPath()
        );
        // Get controller method for this request
        String method = requestProcessorChain.getRequestMethod();
        String path = requestProcessorChain.getRequestPath();
        ControllerMethod controllerMethod = this.pathToMethodMap.get(new RequestPathInfo(method, path));
        if (controllerMethod == null) {
            requestProcessorChain.setResultRenderer(new ResourceNotFoundResultRenderer(method, path));
            return false;
        }
        // Format request content and invoke controller method
        Object result = invokeControllerMethod(controllerMethod, requestProcessorChain.getRequest());
        // Render result
        ResultRenderer resultRenderer = getResultRenderer(controllerMethod, result);
        requestProcessorChain.setResultRenderer(resultRenderer);
        return true;
    }

    /**
     * Build mapping between request path and controller method
     */
    private void buildPathToMethodMap(Set<Class<?>> classSet) {
        if (ValidationUtil.isEmpty(classSet)) return;
        // Go through all classes with @RequestMapping
        for (Class<?> cls : classSet) {
            RequestMapping requestMapping = cls.getAnnotation(RequestMapping.class);
            String basePath = requestMapping == null ? "/" : requestMapping.path();
            if (!basePath.startsWith("/")) {
                basePath = "/" + basePath;
            }

            // Go through all methods with @RequestMapping
            for (Method method : cls.getMethods()) {
                RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                if (methodRequestMapping == null) {
                    continue;
                }

                // Type checking
                if (method.isAnnotationPresent(ResponseBody.class) && !String.class.isAssignableFrom(method.getReturnType())) {
                    log.error("When using @ResponseBody, return type must be String");
                    throw new RuntimeException("Illegal return type: " + method.getReturnType().getName());
                }

                // Build path
                String methodPath = methodRequestMapping.path();
                if (!methodPath.startsWith("/")) {
                    methodPath = "/" + methodPath;
                }
                if (basePath.endsWith("/")) {
                    methodPath = methodPath.substring(1);
                }
                String path = basePath + methodPath;

                // Instantiate RequestPathInfo and ControllerMethod
                RequestPathInfo requestPathInfo = new RequestPathInfo(methodRequestMapping.method().toString(), path);
                if (this.pathToMethodMap.containsKey(requestPathInfo)) {
                    log.warn("Duplicate mapping found for path: {}, overwritten by {}()::{}",
                            path, cls.getName(), method.getName()
                    );
                }
                ControllerMethod controllerMethod = new ControllerMethod(cls, method);
                this.pathToMethodMap.put(requestPathInfo, controllerMethod);
                log.info("{}::{} mapped to {}", cls.getName(), method.getName(), requestPathInfo);
            }
        }
    }

    /**
     * Invoke controller method based on request object
     */
    private Object invokeControllerMethod(ControllerMethod controllerMethod, HttpServletRequest request) throws Throwable {
        // Get controller instance and method
        Object controller = beanContainer.getBean(controllerMethod.getControllerClass());
        Method method = controllerMethod.getControllerMethod();
        // Get body
        String requestBody;
        try {
            requestBody = request.getReader().lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Get parameters (array for values of repeated param keys)
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (log.isDebugEnabled()) {
            log.debug("Invoking {}::{} with params: {} and body: {}",
                    controllerMethod.getControllerClass().getName(),
                    method.getName(),
                    getRequestParameterString(parameterMap),
                    requestBody
            );
        }
        // Get method input parameters
        Object[] methodParameters = getMethodParameters(controllerMethod, parameterMap, requestBody);
        // Invoke controller method
        method.setAccessible(true);
        Object returnValue;
        try {
            if (methodParameters.length == 0) {
                returnValue = method.invoke(controller);
            } else {
                returnValue = method.invoke(controller, methodParameters);
            }
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return returnValue;
    }

    /**
     * Get input parameters of a controller method from data in request object
     */
    private Object[] getMethodParameters(ControllerMethod controllerMethod, Map<String, String[]> requestParameterMap, String requestBody) {
        Method method = controllerMethod.getControllerMethod();
        Object[] methodParameters = new Object[method.getParameterCount()];
        // Go through all method parameters with Annotations
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            RequestBody requestBodyAnno = parameter.getAnnotation(RequestBody.class);
            RequestParam requestParamAnno = parameter.getAnnotation(RequestParam.class);

            if (requestBodyAnno != null) {
                // Get request body class
                Class<?> bodyClass = parameter.getType();
                // Invoke body serializer and check type
                BodySerializer<?> serializer = getBodySerializer(controllerMethod, bodyClass);
                methodParameters[i] = serializer.deserialize(requestBody);
            } else if (requestParamAnno != null) {
                // Get request param name and class
                String paramName = requestParamAnno.name();
                Class<?> paramClass = parameter.getType();
                String[] paramValue = requestParameterMap.get(paramName);
                Object methodParameter;
                if (paramValue == null || paramValue.length == 0) {
                    // Null value/Default value
                    if (requestParamAnno.required()) {
                        throw new WebException("Request query parameter: " + paramName + " is required");
                    }
                    methodParameter = ConversionUtil.primitiveDefaultValue(paramClass);
                } else if (paramClass.isArray()) {
                    // Array (repeated occurrence)
                    Class<?> paramComponentClass = paramClass.getComponentType();
                    methodParameter = Array.newInstance(paramComponentClass, paramValue.length);
                    for (int j = 0; j < paramValue.length; j++) {
                        ((Object[]) methodParameter)[j] =
                                ConversionUtil.convert(paramValue[j], paramComponentClass);
                    }
                } else {
                    // Single value (first occurrence)
                    methodParameter = ConversionUtil.convert(paramValue[0], paramClass);
                }
                methodParameters[i] = methodParameter;
            } else {
                throw new RuntimeException("Parameter: " + parameter.getName() +
                        " must have either @RequestParam or @RequestBody");
            }
        }
        return methodParameters;
    }

    /**
     * Get body serializer based on class field annotation or method annotation, check body type
     */
    private BodySerializer<?> getBodySerializer(ControllerMethod controllerMethod, Class<?> bodyClass) {
        Class<?> controllerClass = controllerMethod.getControllerClass();
        Method method = controllerMethod.getControllerMethod();
        // Get annotated serializer and check type
        if (method.isAnnotationPresent(BodySerializerClass.class)) {
            return getAnnotatedBodySerializer(method, bodyClass);
        } else if (controllerClass.isAnnotationPresent(BodySerializerClass.class)) {
            return getAnnotatedBodySerializer(method, bodyClass);
        }

        // Get default serializer and check type
        if (!bodyClass.isAssignableFrom(String.class)) {
            throw new RuntimeException("DefaultBodySerializer returns String type but controller expects: " +
                    bodyClass.getName());
        }
        if (!beanContainer.containsClass(DefaultBodySerializer.class)) {
            beanContainer.putBean(DefaultBodySerializer.class, new DefaultBodySerializer());
        }
        return (BodySerializer<?>) beanContainer.getBean(DefaultBodySerializer.class);
    }

    private BodySerializer<?> getAnnotatedBodySerializer(AnnotatedElement element, Class<?> bodyClass) {
        Class<?> serializerClass = element.getAnnotation(BodySerializerClass.class).value();
        Class<?> internalClass;
        try {
            internalClass = serializerClass.getMethod("deserialize", String.class).getReturnType();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        if (!bodyClass.isAssignableFrom(internalClass)) {
            throw new RuntimeException(serializerClass.getName() + " returns " + internalClass.getName() +
                    " type but controller expects: " + bodyClass.getName());
        }
        if (!beanContainer.containsClass(serializerClass)) {
            throw new RuntimeException(serializerClass.getName() + " not found in Bean Container");
        }
        return (BodySerializer<?>) beanContainer.getBean(serializerClass);
    }

    /**
     * Get result renderer based on annotation
     */
    private ResultRenderer getResultRenderer(ControllerMethod controllerMethod, Object result) {
        if (result == null) return null;
        Class<?> controllerClass = controllerMethod.getControllerClass();
        Method method = controllerMethod.getControllerMethod();
        ResultRenderer resultRenderer;

        if (method.isAnnotationPresent(ResponseBody.class)) {
            // Must be String
            resultRenderer = new RawResultRenderer((String) result, StatusCode.HTTP_200_OK, MediaType.TEXT_PLAIN);
        } else if (method.isAnnotationPresent(ResponseJsonBody.class)) {
            resultRenderer = new JsonResultRenderer(result);
        } else if (method.isAnnotationPresent(BodySerializerClass.class)) {
            Class<?> serializerClass = method.getAnnotation(BodySerializerClass.class).value();
            resultRenderer = new BodySerializerResultRenderer(result, (BodySerializer<Object>) beanContainer.getBean(serializerClass));
        } else if (controllerClass.isAnnotationPresent(BodySerializerClass.class)) {
            Class<?> serializerClass = controllerClass.getAnnotation(BodySerializerClass.class).value();
            resultRenderer = new BodySerializerResultRenderer(result, (BodySerializer<Object>) beanContainer.getBean(serializerClass));
        } else {
            resultRenderer = new ViewResultRenderer(result);
        }
        return resultRenderer;
    }

    private String getRequestParameterString(Map<String, String[]> parameterMap) {
        List<String> entryString = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            entryString.add(entry.getKey() + "=" + Arrays.toString(entry.getValue()));
        }
        return "{" + String.join(", ", entryString) + "}";
    }
}
