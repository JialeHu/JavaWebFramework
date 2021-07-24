package framework.aop;

import framework.core.BeanContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.packages.service.Service1;
import test.packages.service.Service2;

public class AspectWeaverTest {
    private static final BeanContainer beanContainer = BeanContainer.getInstance();

    @BeforeAll
    static void init() {
        beanContainer.loadBeans("test.packages");
    }

    @Test
    public void doWeaveAspectTest() {
        Assertions.assertNull(((Service1) beanContainer.getBean(Service1.class)).returnNull());
        Assertions.assertNotEquals(null, ((Service1) beanContainer.getBean(Service1.class)).returnObject());
        Assertions.assertThrows(RuntimeException.class, ((Service2) beanContainer.getBean(Service2.class))::returnNull);

        AspectWeaver.doWeaveAspect();

        Assertions.assertEquals("New Return Value", ((Service1) beanContainer.getBean(Service1.class)).returnNull());
        Assertions.assertEquals("New Return Value", ((Service1) beanContainer.getBean(Service1.class)).returnObject());
        Assertions.assertDoesNotThrow(((Service2) beanContainer.getBean(Service2.class))::returnNull);
        Assertions.assertNull(((Service2) beanContainer.getBean(Service2.class)).returnNull());
    }
}
