package framework.core;

import framework.aop.annotation.Aspect;
import framework.core.annotation.Component;
import framework.orm.annotation.Repository;
import framework.web.annotation.Controller;
import framework.core.annotation.Service;
import framework.util.ClassUtil;
import framework.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton container of all beans
 */
@Slf4j
public enum BeanContainer {
    INSTANCE;

    /**
     * @return singleton instance of BeanContainer
     */
    public static BeanContainer getInstance() {
        return BeanContainer.INSTANCE;
    }

    /**
     * List of bean annotations for determining classes to be added
     */
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATIONS = Arrays.asList(
            Component.class,
            Controller.class,
            Repository.class,
            Service.class,
            Aspect.class
    );

    /**
     * Store all beans
     */
    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    private boolean loaded = false;

    /**
     * Scan all classes in the package and load annotated ones into container
     * @param packageName package.name
     */
    public synchronized void loadBeans(String packageName) {
        // Check if loaded
        if (loaded) {
            log.warn("BeanContainer has been loaded.");
            return;
        }

        // Get all classes
        Set<Class<?>> classSet = ClassUtil.getPackageClassSet(packageName);
        if (ValidationUtil.isEmpty(classSet)) {
            log.warn("No class found in package: " + packageName);
            return;
        }

        // Load classes with BEAN_ANNOTAION
        for (Class<?> cls : classSet) {
            for (Class<? extends Annotation> annotation : BEAN_ANNOTATIONS) {
                if (cls.isAnnotationPresent(annotation)) {
                    beanMap.put(cls, ClassUtil.newInstance(cls, true));
                }
            }
        }
        loaded = true;
    }

    /**
     * Get sub classes, exclude self
     * @param interfaceOrClass interface class or class class
     * @return set of sub classes
     */
    public Set<Class<?>> getClassesBySuper(Class<?> interfaceOrClass) {
        // Get all classes
        Set<Class<?>> classSet = getClasses();
        if(ValidationUtil.isEmpty(classSet)){
            log.warn("Empty BeanContainer");
            return null;
        }
        Set<Class<?>> subClassSet = new HashSet<>();
        for(Class<?> cls : classSet){
            // Check if cls is interface of subclass of interfaceOrClass
            if(interfaceOrClass.isAssignableFrom(cls) && !cls.equals(interfaceOrClass)) {
                subClassSet.add(cls);
            }
        }
        return subClassSet.isEmpty() ? null : subClassSet;
    }

    /**
     * Get classes with annotation
     * @param annotation annotation class
     * @return set of classes with annotation
     */
    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation) {
        // Get all classes
        Set<Class<?>> classSet = getClasses();
        if(ValidationUtil.isEmpty(classSet)){
            log.warn("Empty BeanContainer");
            return null;
        }
        Set<Class<?>> annotationClassSet = new HashSet<>();
        for(Class<?> cls : classSet){
            // Check if cls has annotation
            if (cls.isAnnotationPresent(annotation)) {
                annotationClassSet.add(cls);
            }
        }
        return annotationClassSet.isEmpty() ? null : annotationClassSet;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public int size() {
        return beanMap.size();
    }

    public boolean isEmpty() {
        return beanMap.isEmpty();
    }

    public boolean containsClass(Class<?> cls) {
        return beanMap.containsKey(cls);
    }

    public boolean containsBean(Object instance) {
        return beanMap.containsValue(instance);
    }

    public Set<Class<?>> getClasses() {
        return beanMap.keySet();
    }

    public Set<Object> getBeans() {
        return new HashSet<>(beanMap.values());
    }

    public Object getBean(Class<?> cls) {
        return beanMap.get(cls);
    }

    public Object putBean(Class<?> cls, Object instance) {
        return beanMap.put(cls, instance);
    }

    public synchronized void clear() {
        beanMap.clear();
        loaded = false;
    }
}
