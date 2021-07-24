package framework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ClassUtil {
    public static Set<Class<?>> getPackageClassSet(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(packageName.replace(".", "/"));
        if (url == null) {
            log.warn("No class found in package: " + packageName);
            return null;
        }
        Set<Class<?>> classSet = new HashSet<>();
        if (url.getProtocol().equalsIgnoreCase("file")) {
            File packageDirectory = new File(url.getPath());
            getClassFile(classSet, packageDirectory, packageName);
        } else {
            log.warn("Protocol not supported: " + url.getProtocol());
            return null;
        }
        return classSet;
    }

    public static <T> T newInstance(Class<?> cls, boolean accessible) {
        try {
            Constructor<?> constructor = cls.getDeclaredConstructor();
            constructor.setAccessible(accessible);
            return (T) constructor.newInstance();
        } catch (Exception e) {
            log.error("Error getting new instance: ", e);
            throw new RuntimeException(e);
        }
    }

    public static void setField(Object targetBean, Field field, Object fieldValue, boolean accessible) {
        field.setAccessible(accessible);
        try {
            field.set(targetBean, fieldValue);
        } catch (IllegalAccessException e) {
            log.error("Error setting field", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Get all .class file recursively
     * @param classSet class set to be added
     * @param file file source
     * @param packageName package.name
     */
    private static void getClassFile(Set<Class<?>> classSet, File file, String packageName) {
        if (!file.isDirectory()) {
            return;
        }
        // Find class
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    return true;
                }
                String absFilePath = pathname.getAbsolutePath();
                // Load class
                if (absFilePath.endsWith(".class")) {
                    // Format path
                    absFilePath = absFilePath.replace(File.separator, ".");
                    String className = absFilePath.substring(absFilePath.indexOf(packageName));
                    className = className.substring(0, className.lastIndexOf("."));
                    // Add to classSet
                    Class<?> cls = loadClass(className);
                    classSet.add(cls);
                }
                return false;
            }
        });
        // Recurse
        if (files != null) {
            for (File f : files) {
                getClassFile(classSet, f, packageName);
            }
        }
    }

    private static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("Error loading class: ", e);
            throw new RuntimeException();
        }
    }
}
