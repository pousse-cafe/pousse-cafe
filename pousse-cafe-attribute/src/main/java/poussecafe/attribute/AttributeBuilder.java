package poussecafe.attribute;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.adapters.DataAdapters;
import poussecafe.util.StringKey;

public class AttributeBuilder {

    private AttributeBuilder() {

    }

    public static <T> SimpleAttributeBuilder<T> simple(Class<T> valueClass) {
        Objects.requireNonNull(valueClass);
        return new SimpleAttributeBuilder<>(valueClass);
    }

    public static <T> ListAttributeBuilder<T> list(Class<T> elementClass) { // NOSONAR
        Objects.requireNonNull(elementClass);
        ListAttributeBuilder<T> builder = new ListAttributeBuilder<>();
        builder.elementClass = elementClass;
        return builder;
    }

    public static <T> SetAttributeBuilder<T> set(Class<T> elementClass) { // NOSONAR
        Objects.requireNonNull(elementClass);
        SetAttributeBuilder<T> builder = new SetAttributeBuilder<>();
        builder.elementClass = elementClass;
        return builder;
    }

    public static <K, V> MapAttributeBuilder<K, V> map(Class<K> keyClass, Class<V> valueClass) {
        Objects.requireNonNull(keyClass);
        Objects.requireNonNull(valueClass);
        return new MapAttributeBuilder<>();
    }

    public static <T> OptionalAttributeBuilder<T> optional(Class<T> valueClass) { // NOSONAR
        OptionalAttributeBuilder<T> builder = new OptionalAttributeBuilder<>();
        builder.propertyTypeClass = valueClass;
        return builder;
    }

    public static <T extends Number> NumberAttributeBuilder<T> number(Class<T> valueClass) { // NOSONAR
        return new NumberAttributeBuilder<>();
    }

    public static IntegerAttributeBuilder integer() {
        return new IntegerAttributeBuilder();
    }

    public static <T extends StringKey> PrimitiveAttributeBuilder<String, T> stringKey(Class<T> stringKeyClass) {
        return simple(DataAdapters.stringKey(stringKeyClass));
    }

    public static <U, T> PrimitiveAttributeBuilder<U, T> simple(DataAdapter<U, T> dataAdapter) {
        return new PrimitiveAttributeBuilder<>(dataAdapter);
    }

    public static PrimitiveAttributeBuilder<String, OffsetDateTime> offsetDateTime() {
        return simple(DataAdapters.stringOffsetDateTime());
    }

    public static PrimitiveAttributeBuilder<String, LocalDateTime> localDateTime() {
        return simple(DataAdapters.stringLocalDateTime());
    }

    public static PrimitiveAttributeBuilder<String, LocalDate> localDate() {
        return simple(DataAdapters.stringLocalDate());
    }

    public static PrimitiveAttributeBuilder<String, BigDecimal> bigDecimal() {
        return simple(DataAdapters.stringBigDecimal());
    }

    public static <E extends Enum<E>> PrimitiveAttributeBuilder<String, E> enumAttribute(Class<E> enumClass) {
        return simple(DataAdapters.stringEnum(enumClass));
    }
}
