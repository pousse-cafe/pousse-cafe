package poussecafe.util;

import java.lang.reflect.Field;
import java.util.List;

public class ReflectionUtils {

    public static List<Field> getHierarchyFields(Class<?> aClass) {
        return new HierarchyFieldsExtractor(aClass).extractFields();
    }

    public static FieldAccessor access(Object anObject) {
        return new FieldAccessor(anObject);
    }
}
