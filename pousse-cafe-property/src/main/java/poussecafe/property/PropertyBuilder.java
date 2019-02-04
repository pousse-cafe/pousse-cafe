package poussecafe.property;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;
import poussecafe.property.adapters.DataAdapter;
import poussecafe.property.adapters.DataAdapters;
import poussecafe.util.StringKey;

public class PropertyBuilder {

    private PropertyBuilder() {

    }

    public static <T> SimplePropertyBuilder<T> simple(Class<T> valueClass) {
        Objects.requireNonNull(valueClass);
        return new SimplePropertyBuilder<>(valueClass);
    }

    public static <T> ListPropertyBuilder<T> list(Class<T> elementClass) { // NOSONAR
        Objects.requireNonNull(elementClass);
        ListPropertyBuilder<T> builder = new ListPropertyBuilder<>();
        builder.elementClass = elementClass;
        return builder;
    }

    public static <T> SetPropertyBuilder<T> set(Class<T> elementClass) { // NOSONAR
        Objects.requireNonNull(elementClass);
        SetPropertyBuilder<T> builder = new SetPropertyBuilder<>();
        builder.elementClass = elementClass;
        return builder;
    }

    public static <K, V> MapPropertyBuilder<K, V> map(Class<K> keyClass, Class<V> valueClass) {
        Objects.requireNonNull(keyClass);
        Objects.requireNonNull(valueClass);
        return new MapPropertyBuilder<>();
    }

    public static <T> OptionalPropertyBuilder<T> optional(Class<T> valueClass) { // NOSONAR
        OptionalPropertyBuilder<T> builder = new OptionalPropertyBuilder<>();
        builder.propertyTypeClass = valueClass;
        return builder;
    }

    public static <T extends Number> NumberPropertyBuilder<T> number(Class<T> valueClass) { // NOSONAR
        return new NumberPropertyBuilder<>();
    }

    public static IntegerPropertyBuilder integer() {
        return new IntegerPropertyBuilder();
    }

    public static <T extends StringKey> PrimitivePropertyBuilder<String, T> stringKey(Class<T> stringKeyClass) {
        return simple(DataAdapters.stringKey(stringKeyClass));
    }

    public static <U, T> PrimitivePropertyBuilder<U, T> simple(DataAdapter<U, T> dataAdapter) {
        return new PrimitivePropertyBuilder<>(dataAdapter);
    }

    public static PrimitivePropertyBuilder<String, OffsetDateTime> offsetDateTime() {
        return simple(DataAdapters.stringOffsetDateTime());
    }

    public static PrimitivePropertyBuilder<String, LocalDateTime> localDateTime() {
        return simple(DataAdapters.stringLocalDateTime());
    }

    public static PrimitivePropertyBuilder<String, LocalDate> localDate() {
        return simple(DataAdapters.stringLocalDate());
    }

    public static PrimitivePropertyBuilder<String, BigDecimal> bigDecimal() {
        return simple(DataAdapters.stringBigDecimal());
    }

    public static <E extends Enum<E>> PrimitivePropertyBuilder<String, E> enumProperty(Class<E> enumClass) {
        return simple(DataAdapters.stringEnum(enumClass));
    }
}
