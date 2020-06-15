package poussecafe.attribute;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;
import poussecafe.attribute.SingleAttributeBuilder.ExpectingAdaptedReader;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.adapters.DataAdapters;
import poussecafe.util.StringId;

import static java.util.Objects.requireNonNull;

public class AttributeBuilder {

    private AttributeBuilder() {

    }

    public static <T> SingleAttributeBuilder<T> single(Class<T> valueClass) {
        Objects.requireNonNull(valueClass);
        return new SingleAttributeBuilder<>(valueClass);
    }

    public static <T> ListAttributeBuilder<T> list(Class<T> elementClass) {
        return new ListAttributeBuilder<>(elementClass);
    }

    public static <T> SetAttributeBuilder<T> set(Class<T> elementClass) {
        return new SetAttributeBuilder<>(elementClass);
    }

    public static <K, V> MapAttributeBuilder<K, V> map(Class<K> idClass, Class<V> valueClass) {
        Objects.requireNonNull(idClass);
        Objects.requireNonNull(valueClass);
        return new MapAttributeBuilder<>();
    }

    public static <T> OptionalAttributeBuilder<T> optional(Class<T> valueClass) {
        return new OptionalAttributeBuilder<>(valueClass);
    }

    public static <T extends Number> NumberAttributeBuilder<T> number(Class<T> valueClass) {
        requireNonNull(valueClass);
        return new NumberAttributeBuilder<>();
    }

    public static IntegerAttributeBuilder integer() {
        return new IntegerAttributeBuilder();
    }

    public static <T extends StringId> ExpectingAdaptedReader<String, T> stringId(Class<T> stringIdClass) {
        return singleUsingDataAdapter(DataAdapters.stringId(stringIdClass));
    }

    public static <U, T> ExpectingAdaptedReader<U, T> singleUsingDataAdapter(DataAdapter<U, T> dataAdapter) {
        return new DataAdapterBasedSingleAttributeBuilder<>(dataAdapter);
    }

    public static ExpectingAdaptedReader<String, OffsetDateTime> offsetDateTime() {
        return singleUsingDataAdapter(DataAdapters.stringOffsetDateTime());
    }

    public static ExpectingAdaptedReader<String, LocalDateTime> localDateTime() {
        return singleUsingDataAdapter(DataAdapters.stringLocalDateTime());
    }

    public static ExpectingAdaptedReader<String, LocalDate> localDate() {
        return singleUsingDataAdapter(DataAdapters.stringLocalDate());
    }

    public static ExpectingAdaptedReader<String, BigDecimal> bigDecimal() {
        return singleUsingDataAdapter(DataAdapters.stringBigDecimal());
    }

    public static <E extends Enum<E>> ExpectingAdaptedReader<String, E> enumAttribute(Class<E> enumClass) {
        return singleUsingDataAdapter(DataAdapters.stringEnum(enumClass));
    }

    @SuppressWarnings("unchecked")
    public static Class<byte[]> byteArrayClass() {
        return (Class<byte[]>) new byte[] {}.getClass();
    }
}
