package framework.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.packages.component.Component1;
import test.packages.component.Component2;
import test.packages.controller.Controller1;
import test.packages.controller.Controller2;
import test.packages.repository.Repository1;
import test.packages.repository.Repository2;
import test.packages.service.Service1;
import test.packages.service.Service2;

public class DependencyInjectorTest {
    private static final BeanContainer beanContainer = BeanContainer.getInstance();

    @BeforeAll
    static void init() {
        beanContainer.loadBeans("test.packages");
    }

    @Test
    public void doDependencyInjectionTest() {
        Assertions.assertEquals(null, ((Controller1) beanContainer.getBean(Controller1.class)).service1);
        Assertions.assertEquals(null, ((Controller2) beanContainer.getBean(Controller2.class)).service2);
        Assertions.assertEquals(null, ((Service1) beanContainer.getBean(Service1.class)).repository1);
        Assertions.assertEquals(null, ((Service2) beanContainer.getBean(Service2.class)).repository2);
        Assertions.assertEquals(null, ((Service1) beanContainer.getBean(Service1.class)).component1);
        Assertions.assertEquals(null, ((Service2) beanContainer.getBean(Service2.class)).component2);

        DependencyInjector.doDependencyInjection();

        Assertions.assertTrue(((Controller1) beanContainer.getBean(Controller1.class)).service1 instanceof Service1);
        Assertions.assertTrue(((Controller2) beanContainer.getBean(Controller2.class)).service2 instanceof Service2);
        Assertions.assertTrue(((Controller1) beanContainer.getBean(Controller1.class)).service1.component1 instanceof Component1);
        Assertions.assertTrue(((Controller2) beanContainer.getBean(Controller2.class)).service2.component2 instanceof Component2);
        Assertions.assertTrue(((Controller1) beanContainer.getBean(Controller1.class)).service1.repository1 instanceof Repository1);
        Assertions.assertTrue(((Controller2) beanContainer.getBean(Controller2.class)).service2.repository2 instanceof Repository2);
    }
}
