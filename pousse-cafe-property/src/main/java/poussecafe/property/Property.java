package poussecafe.property;

public interface Property<T> {

    T get();

    void set(T value);

    default void setValueOf(Property<T> property) {
        set(property.get());
    }

    default boolean valueEqualsValueOf(Property<T> property) {
        return valueEquals(property.get());
    }

    default boolean valueEquals(T value) {
        return get().equals(value);
    }
}
