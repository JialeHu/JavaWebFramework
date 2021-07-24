package test.packages.aspect;

import framework.aop.annotation.Aspect;
import framework.aop.annotation.Order;
import framework.aop.aspect.AbstractAspect;

import java.lang.reflect.Method;

@Aspect(pointcut = "execution(* test.packages.service.Service1.*(..))")
@Order(1)
public class Aspect1 extends AbstractAspect {
    @Override
    public void before(Class<?> targetClass, Method targetMethod, Object[] args) {
        System.out.println("Aspect 1 before invoking: " + targetMethod.getName() + " in: " + targetClass.getName());
    }

    @Override
    public Object afterReturning(Class<?> targetClass, Method targetMethod, Object[] args, Object returnValue) {
        System.out.println("Aspect 1 after returning from: " + targetMethod.getName() + " in: " + targetClass.getName());
        return "New Return Value";
    }

}
