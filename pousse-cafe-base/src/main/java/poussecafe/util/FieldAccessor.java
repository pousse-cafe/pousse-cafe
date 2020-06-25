package poussecafe.util;

import java.util.Objects;

public class FieldAccessor {

    public FieldAccessor(Object target) {
        Objects.requireNonNull(target);
        this.target = target;
    }

    private Object target;

    /**
     * @deprecated use new FieldAccessor(target).instanceField(fieldName).get()
     */
    @Deprecated(since = "0.20")
    public static Object getFieldValue(Object target,
            String fieldName) {
        return instanceField(target, fieldName).get();
    }

    private static InstanceField instanceField(Object instance, String fieldName) {
        return new InstanceField.Builder()
                .instance(instance)
                .fieldName(fieldName)
                .build();
    }

    /**
     * @deprecated use new FieldAccessor(target).instanceField(fieldName).set(value)
     */
    @Deprecated(since = "0.20")
    public static void setFieldValue(Object target,
            String fieldName,
            Object value) {
        instanceField(target, fieldName).set(value);
    }

    /**
     * @deprecated use instanceField(fieldName).get()
     */
    @Deprecated(since = "0.20")
    public Object get(String fieldName) {
        return instanceField(fieldName).get();
    }

    public InstanceField instanceField(String fieldName) {
        return new InstanceField.Builder()
                .instance(target)
                .fieldName(fieldName)
                .build();
    }

    /**
     * @deprecated use instanceField(fieldName).set(value)
     */
    @Deprecated(since = "0.20")
    public void set(String fieldName,
            Object value) {
        instanceField(fieldName).set(value);
    }

    /**
     * @deprecated use instanceField(fieldName).isPresent()
     */
    @Deprecated(since = "0.20")
    public boolean isPresent(String fieldName) {
        return instanceField(fieldName).isPresent();
    }
}
