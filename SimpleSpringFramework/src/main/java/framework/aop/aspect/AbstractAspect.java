package framework.aop.aspect;

import java.lang.reflect.Method;

public abstract class AbstractAspect {
    /**
     * Apply advice before method execution
     * @param targetClass the class whose method to be modified
     * @param targetMethod the method to be modified
     * @param args the input arguments to the targetMethod
     * @throws Throwable any exception to be thrown
     */
    public void before(Class<?> targetClass, Method targetMethod, Object[] args) throws Throwable {}

    /**
     * Apply advice after method return
     * @param targetClass the class whose method to be modified
     * @param targetMethod the method to be modified
     * @param args the input arguments to the targetMethod
     * @param returnValue the return value of the target Method
     * @return returnValue
     * @throws Throwable any exception to be thrown
     */
    public Object afterReturning(Class<?> targetClass, Method targetMethod, Object[] args, Object returnValue) throws Throwable {
        return returnValue;
    }

    /**
     * Apply advice after exception being thrown from method or afterReturn advice
     * @param targetClass the class whose method to be modified
     * @param targetMethod the method to be modified
     * @param args the input arguments to the targetMethod
     * @param e the exception thrown from the targetMethod
     * @throws Throwable any exception to be thrown
     */
    public void afterThrowing(Class<?> targetClass, Method targetMethod, Object[] args, Throwable e) throws Throwable {
        throw e;
    }
}
