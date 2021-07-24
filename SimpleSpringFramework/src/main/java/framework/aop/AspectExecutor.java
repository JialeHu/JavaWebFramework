package framework.aop;

import framework.aop.aspect.AspectInfo;
import framework.util.ValidationUtil;
import lombok.Getter;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.*;

public class AspectExecutor implements MethodInterceptor {
    private final Class<?> targetClass;
    @Getter
    private final List<AspectInfo> sortedAspectInfos;

    public AspectExecutor(Class<?> targetClass, List<AspectInfo> aspectInfos) {
        this.targetClass = targetClass;
        // Sort aspectInfoList ascending
        List<AspectInfo> aspectInfosCopy = new ArrayList<>(aspectInfos);
        Collections.sort(aspectInfosCopy, Comparator.comparingInt(AspectInfo::getAspectOrder));
        this.sortedAspectInfos = aspectInfosCopy;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // Find exact matches
        removeUnmatchedAspectInfos(method);
        if (ValidationUtil.isEmpty(sortedAspectInfos)) {
            // Invoke original method
            return methodProxy.invokeSuper(obj, args);
        }
        Object returnValue = null;
        // Apply before advice
        applyBeforeAdvice(method, args);
        try {
            // Invoke original method
            returnValue = methodProxy.invokeSuper(obj, args);
            // Apply afterReturn advice (in reverse order)
            returnValue = applyAfterReturningAdvice(method, args, returnValue);
        } catch (Throwable e) {
            // Apply afterThrow advice (in reverse order)
            applyAfterThrowingAdvice(method, args, e);
        }
        return returnValue;
    }

    private void applyBeforeAdvice(Method method, Object[] args) throws Throwable {
        for (AspectInfo aspectInfo : sortedAspectInfos) {
            aspectInfo.getAspectInstance().before(targetClass, method, args);
        }
    }

    private Object applyAfterReturningAdvice(Method method, Object[] args, Object returnValue) throws Throwable {
        for (int i = sortedAspectInfos.size() - 1; i >= 0; i--) {
            returnValue = sortedAspectInfos.get(i).getAspectInstance().afterReturning(targetClass, method, args, returnValue);
        }
        return returnValue;
    }

    private void applyAfterThrowingAdvice(Method method, Object[] args, Throwable e) throws Throwable {
        for (int i = sortedAspectInfos.size() - 1; i >= 0; i--) {
            sortedAspectInfos.get(i).getAspectInstance().afterThrowing(targetClass, method, args, e);
        }
    }

    private void removeUnmatchedAspectInfos(Method method) {
        Iterator<AspectInfo> itr = sortedAspectInfos.iterator();
        while(itr.hasNext()) {
            if (!itr.next().getPointcutLocator().isMatch(method)) {
                itr.remove();
            }
        }
    }
}
