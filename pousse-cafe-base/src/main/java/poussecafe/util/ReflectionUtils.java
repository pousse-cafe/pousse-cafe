package poussecafe.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import poussecafe.exception.PousseCafeException;

public class ReflectionUtils {

    private ReflectionUtils() {

    }

    public static List<Field> getHierarchyFields(Class<?> aClass) {
        return new HierarchyFieldsExtractor(aClass).extractFields();
    }

    public static FieldAccessor access(Object anObject) {
        return new FieldAccessor(anObject);
    }

    public static boolean isAbstract(Class<?> aClass) {
        return aClass.isInterface() || Modifier.isAbstract(aClass.getModifiers());
    }

    public static <T> T newInstance(Class<T> aClass) {
        try {
            return aClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new PousseCafeException("Unable to instantiate class", e);
        }
    }

    public static Method method(Class<?> aClass, String methodName, Class<?>... parameterTypes) {
        try {
            return aClass.getDeclaredMethod(methodName, parameterTypes);
        } catch (Exception e) {
            throw new PousseCafeException("Unable to retrieve method", e);
        }
    }
}
