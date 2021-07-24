package app.demo;

import framework.boot.ApplicationStarter;
import framework.web.WebApplication;

/**
 * Entrypoint of App.
 */
public class DemoWebApplication extends WebApplication {
    public static void main(String[] args) {
        ApplicationStarter.start(DemoWebApplication.class, args);
    }
}
