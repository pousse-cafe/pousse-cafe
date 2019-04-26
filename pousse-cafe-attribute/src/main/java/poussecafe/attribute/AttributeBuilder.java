package poussecafe.attribute;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.adapters.DataAdapters;
import poussecafe.util.StringId;

public class AttributeBuilder {

    private AttributeBuilder() {

    }

    public static <T> SingleAttributeBuilder<T> single(Class<T> valueClass) {
        Objects.requireNonNull(valueClass);
        return new SingleAttributeBuilder<>(valueClass);
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

    public static <K, V> MapAttributeBuilder<K, V> map(Class<K> idClass, Class<V> valueClass) {
        Objects.requireNonNull(idClass);
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

    public static <T extends StringId> PrimitiveAttributeBuilder<String, T> stringId(Class<T> stringIdClass) {
        return single(DataAdapters.stringId(stringIdClass));
    }

    public static <U, T> PrimitiveAttributeBuilder<U, T> single(DataAdapter<U, T> dataAdapter) {
        return new PrimitiveAttributeBuilder<>(dataAdapter);
    }

    public static PrimitiveAttributeBuilder<String, OffsetDateTime> offsetDateTime() {
        return single(DataAdapters.stringOffsetDateTime());
    }

    public static PrimitiveAttributeBuilder<String, LocalDateTime> localDateTime() {
        return single(DataAdapters.stringLocalDateTime());
    }

    public static PrimitiveAttributeBuilder<String, LocalDate> localDate() {
        return single(DataAdapters.stringLocalDate());
    }

    public static PrimitiveAttributeBuilder<String, BigDecimal> bigDecimal() {
        return single(DataAdapters.stringBigDecimal());
    }

    public static <E extends Enum<E>> PrimitiveAttributeBuilder<String, E> enumAttribute(Class<E> enumClass) {
        return single(DataAdapters.stringEnum(enumClass));
    }

    @SuppressWarnings("unchecked")
    public static Class<byte[]> byteArrayClass() {
        return (Class<byte[]>) new byte[] {}.getClass();
    }
}
