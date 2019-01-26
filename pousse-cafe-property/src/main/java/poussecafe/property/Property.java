package poussecafe.property;

public interface Property<T> {

    T get();

    void set(T value);

    default void setValueOf(Property<T> property) {
        set(property.get());
    }
}
