package framework.boot;

import framework.core.BeanContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.packages.Application1;
import test.packages.Application2;

public class BaseApplicationTest {
    private static final BeanContainer beanContainer = BeanContainer.getInstance();

    @BeforeEach
    void init() {
        beanContainer.clear();
    }

    @AfterEach
    void end() {
        beanContainer.clear();
    }

    @Test
    public void defaultPackageTest() {
        ApplicationStarter.start(Application1.class, null);
        Assertions.assertEquals(10, beanContainer.size());
    }

    @Test
    public void annotatedPackageTest() {
        ApplicationStarter.start(Application2.class, null);
        Assertions.assertEquals(2, BeanContainer.getInstance().size());
    }

}
