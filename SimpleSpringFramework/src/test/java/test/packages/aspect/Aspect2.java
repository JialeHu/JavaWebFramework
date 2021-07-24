package test.packages.aspect;

import framework.aop.annotation.Aspect;
import framework.aop.annotation.Order;
import framework.aop.aspect.AbstractAspect;

import java.lang.reflect.Method;

@Aspect(pointcut = "execution(* test.packages.service.*.*(..))")
@Order(2)
public class Aspect2 extends AbstractAspect {
    @Override
    public void before(Class<?> targetClass, Method targetMethod, Object[] args) {
        System.out.println("Aspect 2 before invoking: " + targetMethod.getName() + " in: " + targetClass.getName());
    }

    @Override
    public Object afterReturning(Class<?> targetClass, Method targetMethod, Object[] args, Object returnValue) {
        System.out.println("Aspect 2 after returning from: " + targetMethod.getName() + " in: " + targetClass.getName());
        return returnValue;
    }

    @Override
    public void afterThrowing(Class<?> targetClass, Method targetMethod, Object[] args, Throwable e) {
        System.out.println("Aspect 2 after throwing from: " + targetMethod.getName() + " in: " + targetClass.getName());
    }
}
