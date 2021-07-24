package framework.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConversionUtil {

    private static final Set<Class<?>> primitiveNumberClasses = new HashSet<>(Arrays.asList(
            byte.class, short.class, int.class, long.class, float.class, double.class
    ));

    public static Object primitiveDefaultValue(Class<?> type) {
        if (primitiveNumberClasses.contains(type)) {
            return 0;
        } else if (type == boolean.class) {
            return false;
        }
        return null;
    }

    public static Object convert(String value, Class<?> targetType) {
        if (ValidationUtil.isEmpty(value)) {
            return primitiveDefaultValue(targetType);
        }
        if (targetType.equals(String.class)) {
            return value;
        } else if (targetType == byte.class) {
            return Byte.parseByte(value);
        } else if (targetType == Byte.class) {
            return Byte.valueOf(value);
        } else if (targetType == short.class) {
            return Short.parseShort(value);
        } else if (targetType == Short.class) {
            return Short.valueOf(value);
        } else if (targetType == int.class) {
            return Integer.parseInt(value);
        } else if (targetType == Integer.class) {
            return Integer.valueOf(value);
        } else if (targetType == long.class) {
            return Long.parseLong(value);
        } else if (targetType == Long.class) {
            return Long.valueOf(value);
        } else if (targetType == float.class) {
            return Float.parseFloat(value);
        } else if (targetType == Float.class) {
            return Float.valueOf(value);
        } else if (targetType == double.class) {
            return Double.parseDouble(value);
        } else if (targetType == Double.class) {
            return Double.valueOf(value);
        }

        throw new RuntimeException("Unsupported conversion targetType: " + targetType.getName());
    }
}
