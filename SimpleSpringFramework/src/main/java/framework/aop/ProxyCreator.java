package framework.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class ProxyCreator {
    public static Object createProxy(Class<?> targetClass, MethodInterceptor aspectExecutor) {
        return Enhancer.create(targetClass, aspectExecutor);
    }
}
