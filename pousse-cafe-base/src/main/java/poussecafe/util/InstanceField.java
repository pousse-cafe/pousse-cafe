package poussecafe.util;

import java.lang.reflect.Field;

import static java.util.Objects.requireNonNull;

public class InstanceField {

    public Object get() {
        try {
            return unprotectedField().get(instance);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new ReflectionException("Unable to get value from field", e);
        }
    }

    private Object instance;

    private Field unprotectedField() {
        try {
            Field field = searchFieldInHierarchy();
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
            throw new ReflectionException("Unable to get field", e);
        }
    }

    private Field searchFieldInHierarchy() throws NoSuchFieldException {
        Class<?> currentClass = instance.getClass();
        while(currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException();
    }

    private String fieldName;

    public void set(Object value) {
        try {
            unprotectedField().set(instance, value);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new ReflectionException("Unable to set field value", e);
        }
    }

    public boolean isPresent() {
        try {
            searchFieldInHierarchy();
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public static class Builder {

        private InstanceField field = new InstanceField();

        public InstanceField build() {
            requireNonNull(field.instance);
            requireNonNull(field.fieldName);
            return field;
        }

        public Builder instance(Object instance) {
            field.instance = instance;
            return this;
        }

        public Builder fieldName(String fieldName) {
            field.fieldName = fieldName;
            return this;
        }
    }

    private InstanceField() {

    }
}
