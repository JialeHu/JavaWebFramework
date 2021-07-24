package app.demo.aspect;

import framework.aop.annotation.Aspect;
import framework.aop.annotation.Order;
import framework.aop.aspect.AbstractAspect;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * Demo aspect class for recording elapsed time of services.
 */
@Slf4j
@Aspect(pointcut = "execution(* app.demo.service.*.*(..))")
@Order(1)
public class TimerAspect extends AbstractAspect {

    private long startTime;

    /**
     * Start timer before function execution.
     * @param targetClass the class whose method to be modified
     * @param targetMethod the method to be modified
     * @param args the input arguments to the targetMethod
     */
    @Override
    public void before(Class<?> targetClass, Method targetMethod, Object[] args) {
        startTime = System.currentTimeMillis();
    }

    /**
     * Stop timer after success function execution.
     * @param targetClass the class whose method to be modified
     * @param targetMethod the method to be modified
     * @param args the input arguments to the targetMethod
     * @param returnValue the return value of the target Method
     * @return
     */
    @Override
    public Object afterReturning(Class<?> targetClass, Method targetMethod, Object[] args, Object returnValue) {
        log.info("{}::{} took {} ms to run",
                targetClass.getName(),
                targetMethod.getName(),
                System.currentTimeMillis() - startTime
        );
        return returnValue;
    }

    /**
     * Stop timer after exception being thrown.
     * @param targetClass the class whose method to be modified
     * @param targetMethod the method to be modified
     * @param args the input arguments to the targetMethod
     * @param e the exception thrown from the targetMethod
     * @throws Throwable
     */
    @Override
    public void afterThrowing(Class<?> targetClass, Method targetMethod, Object[] args, Throwable e) throws Throwable {
        log.info("{}::{} took {} ms to throw {}",
                targetClass.getName(),
                targetMethod.getName(),
                System.currentTimeMillis() - startTime,
                e.toString()
        );
        throw e;
    }

}
