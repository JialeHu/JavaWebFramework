package framework.core;

import framework.core.annotation.Autowired;
import framework.util.ClassUtil;
import framework.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Set;

@Slf4j
public class DependencyInjector {
    private static final BeanContainer beanContainer = BeanContainer.getInstance();

    /**
     * Inject dependent value from beanContainer into fields annotated by Autowired
     */
    public static void doDependencyInjection() {
        if(ValidationUtil.isEmpty(beanContainer.getClasses())){
            log.warn("No class in BeanContainer");
            return;
        }
        // Find fields to be autowired
        for (Class<?> cls : beanContainer.getClasses()) {
            for (Field field : cls.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String dependedClass = autowired.to();
                    // Get field type
                    Class<?> fieldClass = field.getType();
                    // Get instance from bean container
                    Object fieldValue = getFieldInstance(fieldClass, dependedClass);
                    if (fieldValue == null) {
                        throw new RuntimeException("Cannot inject value into field: " + field.getName() +
                                " with target type: " + fieldClass.getName());
                    } else {
                        // Inject value with Reflection
                        Object targetBean = beanContainer.getBean(cls);
                        ClassUtil.setField(targetBean, field, fieldValue, true);
                    }
                }
            }
        }
    }

    /**
     * Get implementation instance of a field
     */
    private static Object getFieldInstance(Class<?> fieldClass, String dependedClass) {
        Object fieldValue = beanContainer.getBean(fieldClass);
        if (fieldValue != null){
            // Found bean directly
            return fieldValue;
        } else {
            // Find based on interface and Autowired input
            Class<?> implementedClass = getImplementedClass(fieldClass, dependedClass);
            if (implementedClass != null) {
                return beanContainer.getBean(implementedClass);
            } else {
                return null;
            }
        }
    }

    /**
     * Get implementation class of an interface
     */
    private static Class<?> getImplementedClass(Class<?> fieldClass, String dependedClass) {
        Set<Class<?>> classSet = beanContainer.getClassesBySuper(fieldClass);
        if (!ValidationUtil.isEmpty(classSet)) {
            if (ValidationUtil.isEmpty(dependedClass)) {
                // Find by interface
                if(classSet.size() == 1){
                    return classSet.iterator().next();
                } else {
                    // Throw if more than one implementation is found
                    throw new RuntimeException("Multiple implemented classes for " + fieldClass.getName() +
                            " please specify by setting @Autowired's dependedClass");
                }
            } else {
                // Find by Autowired input
                for (Class<?> cls : classSet) {
                    if (dependedClass.equals(cls.getSimpleName())) {
                        return cls;
                    }
                }
            }
        }
        return null;
    }

}
