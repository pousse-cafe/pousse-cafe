package poussecafe.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class ReflectionUtils {

    public static List<Field> getHierarchyFields(Class<?> aClass) {
        List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = aClass;
        while(currentClass != null && currentClass != Object.class) {
            allFields.addAll(asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    public static FieldAccessor access(Object anObject) {
        return new FieldAccessor(anObject);
    }
}
