package poussecafe.util;

import java.lang.reflect.Field;
import poussecafe.exception.PousseCafeException;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class FieldAccessor {

    private Object target;

    public FieldAccessor(Object target) {
        setTarget(target);
    }

    public static Object getFieldValue(Object target,
            String fieldName) {
        return new FieldAccessor(target).get(fieldName);
    }

    public static void setFieldValue(Object target,
            String fieldName,
            Object value) {
        new FieldAccessor(target).set(fieldName, value);
    }

    private void setTarget(Object target) {
        checkThat(value(target).notNull().because("Target cannot be null"));
        this.target = target;
    }

    public Object get(String fieldName) {
        try {
            return getUnprotectedField(fieldName).get(target);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new PousseCafeException("Unable to get value from field", e);
        }
    }

    private Field getUnprotectedField(String fieldName) {
        Class<?> targetClass = target.getClass();
        try {
            Field field = searchField(targetClass, fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
            throw new PousseCafeException("Unable to get field", e);
        }
    }

    private Field searchField(Class<?> targetClass, String fieldName) throws NoSuchFieldException {
        Class<?> currentClass = targetClass;
        while(currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = targetClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException();
    }

    public void set(String fieldName,
            Object value) {
        try {
            getUnprotectedField(fieldName).set(target, value);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new PousseCafeException("Unable to set field value", e);
        }
    }
}
