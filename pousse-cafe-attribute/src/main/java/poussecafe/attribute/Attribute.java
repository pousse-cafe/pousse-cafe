package poussecafe.attribute;

public interface Attribute<T> {

    T value();

    void value(T value);

    default void valueOf(Attribute<T> property) {
        value(property.value());
    }

    @Deprecated(since = "0.19")
    default boolean valueEqualsValueOf(Attribute<T> property) {
        return valueEquals(property.value());
    }

    @Deprecated(since = "0.19")
    default boolean valueEquals(T value) {
        return value().equals(value);
    }
}
