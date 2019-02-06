package poussecafe.attribute;

public interface Attribute<T> {

    T value();

    void value(T value);

    default void valueOf(Attribute<T> property) {
        value(property.value());
    }

    default boolean valueEqualsValueOf(Attribute<T> property) {
        return valueEquals(property.value());
    }

    default boolean valueEquals(T value) {
        return value().equals(value);
    }
}
