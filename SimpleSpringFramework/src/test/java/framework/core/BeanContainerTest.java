package framework.core;

import framework.core.annotation.Component;
import framework.orm.annotation.Repository;
import framework.web.annotation.Controller;
import framework.core.annotation.Service;
import org.junit.jupiter.api.*;
import test.packages.component.Component1;
import test.packages.component.Component2;
import test.packages.controller.Controller1;
import test.packages.repository.Repository1;
import test.packages.service.Service1;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BeanContainerTest {
    private static final BeanContainer beanContainer = BeanContainer.getInstance();

    @Test
    @Order(1)
    @DisplayName("Test if annotated beans are loaded")
    public void loadBeansTest() {
        beanContainer.loadBeans("test.packages");
        Assertions.assertEquals(10, beanContainer.size());
        System.out.println(beanContainer.getClasses());
    }

    @Test
    @Order(2)
    public void getBeanTest() {
        Component1 c1 = (Component1) beanContainer.getBean(Component1.class);
        Assertions.assertTrue(c1 instanceof Component1);
        Controller1 ctl1 = (Controller1) beanContainer.getBean(Controller1.class);
        Assertions.assertTrue(ctl1 instanceof Controller1);
        Repository1 r1 = (Repository1) beanContainer.getBean(Repository1.class);
        Assertions.assertTrue(r1 instanceof Repository1);
        Service1 s1 = (Service1) beanContainer.getBean(Service1.class);
        Assertions.assertTrue(s1 instanceof Service1);
    }

    @Test
    @Order(3)
    public void getClassesBySuperTest() {
        Component1 c1 = (Component1) beanContainer.getBean(Component1.class);
        Assertions.assertTrue(c1 instanceof Component1);
        Component1 c2 = (Component2) beanContainer.getBean(Component2.class);
        Assertions.assertTrue(c2 instanceof Component2);
        Assertions.assertTrue(beanContainer.getClassesBySuper(Component1.class).contains(Component2.class));
    }

    @Test
    @Order(4)
    public void getClassesByAnnotationTest() {
        Assertions.assertEquals(2, beanContainer.getClassesByAnnotation(Component.class).size());
        Assertions.assertEquals(2, beanContainer.getClassesByAnnotation(Controller.class).size());
        Assertions.assertEquals(2, beanContainer.getClassesByAnnotation(Repository.class).size());
        Assertions.assertEquals(2, beanContainer.getClassesByAnnotation(Service.class).size());
    }
}
