package poussecafe.storable;

import poussecafe.domain.Entity;

import static poussecafe.check.Checks.checkThatValue;

public class PropertyBuilder {

    public static <T> SimplePropertyBuilder<T> simple(Class<T> valueClass) {
        return new SimplePropertyBuilder<>();
    }

    public static <T> ListPropertyBuilder<T> list(Class<T> elementClass) {
        return new ListPropertyBuilder<>();
    }

    public static <T> SetPropertyBuilder<T> set(Class<T> elementClass) {
        return new SetPropertyBuilder<>();
    }

    public static <K, V> MapPropertyBuilder<K, V> map(Class<K> keyClass, Class<V> valueClass) {
        checkThatValue(keyClass).notNull();
        checkThatValue(valueClass).notNull();
        return new MapPropertyBuilder<>();
    }

    public static <T> OptionalPropertyBuilder<T> optional(Class<T> valueClass) {
        return new OptionalPropertyBuilder<>();
    }

    public static <T extends Number> NumberPropertyBuilder<T> number(Class<T> valueClass) {
        return new NumberPropertyBuilder<>();
    }

    public static <K, E extends Entity<K, ?>> EntityMapPropertyBuilder<K, E> entityMap(Class<K> entityKeyClass, Class<E> entityClass) {
        checkThatValue(entityKeyClass).notNull();
        checkThatValue(entityClass).notNull();
        return new EntityMapPropertyBuilder<>(entityClass);
    }
}
