package poussecafe.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class HierarchyFieldsExtractor {

    public HierarchyFieldsExtractor(Class<?> aClass) {
        this.aClass = aClass;
    }

    private Class<?> aClass;

    public List<Field> extractFields() {
        List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = aClass;
        while(currentClass != null && currentClass != Object.class) {
            allFields.addAll(asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }
}
