package framework.boot;

import framework.aop.AspectWeaver;
import framework.aop.annotation.Aspect;
import framework.boot.annotation.ApplicationScope;
import framework.core.BeanContainer;
import framework.core.DependencyInjector;
import framework.core.annotation.Component;
import framework.web.annotation.Controller;
import framework.core.annotation.Service;
import framework.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * Base Application supporting Bean Container IoC and AOP
 */
@Slf4j
public class BaseApplication {

    protected final String rootPackage;

    public BaseApplication() {
        rootPackage = getApplicationRootPackage();
    }

    /**
     * Initialize Application, override to append custom init
     * @throws Exception
     */
    protected void init() throws Exception {
        log.info("Starting Application with root package: " + rootPackage);
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans(rootPackage);
        AspectWeaver.doWeaveAspect();
        DependencyInjector.doDependencyInjection();
        logStatus();

        // Handling Shutting Down (System Signal)
        Runtime.getRuntime().addShutdownHook(new Thread("shutdown-thread") {
            @Override
            public void run() {
                log.info("\n");
                log.info("Shutting Down ...");
                try {
                    int exitCode = BaseApplication.this.cleanup();
                    log.info("Finish Shutting Down");
                    Runtime.getRuntime().halt(exitCode);
                } catch (Exception e) {
                    log.error("Error shutting down: " + e);
                }
            }
        });
    }

    /**
     * Cleanup after shutting down is triggered
     * @return error code
     * @throws Exception
     */
    protected int cleanup() throws Exception {
        return 0;
    }

    private String getApplicationRootPackage() {
        // Get annotated package name
        ApplicationScope applicationScope = this.getClass().getAnnotation(ApplicationScope.class);
        if (applicationScope != null) {
            String packageName = applicationScope.rootPackage().trim();
            if (packageName.length() > 0) {
                return packageName;
            }
        }
        // Get application class package name
        return this.getClass().getPackage().getName();
    }

    private void logStatus() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        log.info("Loaded @Component are: ");
        Set<Class<?>> classes = beanContainer.getClassesByAnnotation(Component.class);
        if (!ValidationUtil.isEmpty(classes)) {
            for (Class<?> cls : classes) {
                log.info("    " + cls.getName());
            }
        }
        log.info("Loaded @Controller are: ");
        classes = beanContainer.getClassesByAnnotation(Controller.class);
        if (!ValidationUtil.isEmpty(classes)) {
            for (Class<?> cls : classes) {
                log.info("    " + cls.getName());
            }
        }
        log.info("Loaded @Service are: ");
        classes = beanContainer.getClassesByAnnotation(Service.class);
        if (!ValidationUtil.isEmpty(classes)) {
            for (Class<?> cls : classes) {
                log.info("    " + cls.getName());
            }
        }
        log.info("Loaded @Aspect are: ");
        classes = beanContainer.getClassesByAnnotation(Aspect.class);
        if (!ValidationUtil.isEmpty(classes)) {
            for (Class<?> cls : classes) {
                log.info("    " + cls.getName());
            }
        }
    }
}
