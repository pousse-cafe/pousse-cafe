package poussecafe.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

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
}
