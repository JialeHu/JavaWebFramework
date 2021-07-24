package framework.util;

import java.util.Collection;

public class ValidationUtil {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }
}
