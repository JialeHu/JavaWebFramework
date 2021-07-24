package framework.aop;

import framework.aop.annotation.Aspect;
import framework.aop.annotation.Order;
import framework.aop.aspect.AbstractAspect;
import framework.aop.aspect.AspectInfo;
import framework.core.BeanContainer;
import framework.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class AspectWeaver {
    private static final BeanContainer beanContainer = BeanContainer.getInstance();

    public static void doWeaveAspect() {
        // Get all classes with @Aspect annotation
        Set<Class<?>> aspectClassSet = beanContainer.getClassesByAnnotation(Aspect.class);
        if (ValidationUtil.isEmpty(aspectClassSet)) return;
        log.info("Found Aspect Classes: {}", aspectClassSet);
        // Get list of AspectInfo
        List<AspectInfo> aspectInfos = getAspectInfos(aspectClassSet);
        // Find classes to be weaved
        for (Class<?> cls : beanContainer.getClasses()) {
            // Exclude aspectClass
            if (aspectClassSet.contains(cls)) continue;
            // Get aspect could be weaved
            List<AspectInfo> couldMatchAspects = getCouldMatchAspectInfos(aspectInfos, cls);
            // Weave matched aspects in
            weaveMatchAspects(couldMatchAspects, cls);
        }
    }

    private static void weaveMatchAspects(List<AspectInfo> couldMatchAspects, Class<?> targetClass) {
        if (ValidationUtil.isEmpty(couldMatchAspects)) return;
        // Create method interceptor and dynamic proxy for targetClass
        AspectExecutor aspectExecutor = new AspectExecutor(targetClass, couldMatchAspects);
        log.info("Creating proxy of {}, could match: {}", targetClass.getName(), couldMatchAspects);
        Object proxyBean = ProxyCreator.createProxy(targetClass, aspectExecutor);
        // Replace existing bean by proxyBean
        beanContainer.putBean(targetClass, proxyBean);
    }

    /**
     * Get a list of aspectInfo
     * @param aspectClassSet a set of classes to find aspectInfo
     * @return List of aspectInfo
     */
    private static List<AspectInfo> getAspectInfos(Set<Class<?>> aspectClassSet) {
        List<AspectInfo> aspectInfos = new ArrayList<>();
        for (Class<?> cls : aspectClassSet) {
            if (isAspectClassValid(cls)) {
                // Get annotations and instance
                Aspect aspectAnno = cls.getAnnotation(Aspect.class);
                Order orderAnno = cls.getAnnotation(Order.class);
                AbstractAspect aspectInstance = (AbstractAspect) beanContainer.getBean(cls);
                // Initialize a pointcut expression locator
                PointcutLocator pointcutLocator = new PointcutLocator(aspectAnno.pointcut());
                // Create aspectInfo and add to list
                AspectInfo aspectInfo = new AspectInfo(orderAnno.value(), aspectInstance, pointcutLocator);
                aspectInfos.add(aspectInfo);
            } else {
                throw new RuntimeException("@Aspect and @Order must be added to: " + cls.getName() +
                        ", and it must extent from AbstractAspect");
            }
        }
        return aspectInfos;
    }

    private static List<AspectInfo> getCouldMatchAspectInfos(List<AspectInfo> aspectInfos, Class<?> cls) {
        List<AspectInfo> couldMatchAspectInfos = new ArrayList<>();
        for (AspectInfo aspectInfo : aspectInfos) {
            if (aspectInfo.getPointcutLocator().couldMatch(cls)) {
                couldMatchAspectInfos.add(aspectInfo);
            }
        }
        return couldMatchAspectInfos;
    }

    /**
     * Check if a class is valid for Weaving Aspect
     * @param aspectClass class to be checked
     * @return validity
     */
    private static boolean isAspectClassValid(Class<?> aspectClass) {
        return aspectClass.isAnnotationPresent(Aspect.class) &&
                aspectClass.isAnnotationPresent(Order.class) &&
                AbstractAspect.class.isAssignableFrom(aspectClass);
    }
}
