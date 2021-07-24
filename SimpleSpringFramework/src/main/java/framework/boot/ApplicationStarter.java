package framework.boot;

public class ApplicationStarter {
    public static void start(Class<? extends BaseApplication> applicationClass, String[] args) {
        try {
            applicationClass.getConstructor().newInstance().init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
