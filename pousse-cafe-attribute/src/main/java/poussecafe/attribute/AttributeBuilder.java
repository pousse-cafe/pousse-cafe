package poussecafe.attribute;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.adapters.DataAdapters;
import poussecafe.attribute.list.ListAttributeBuilder;
import poussecafe.attribute.map.MapAttributeBuilder;
import poussecafe.attribute.number.IntegerAttributeBuilder;
import poussecafe.attribute.number.NumberAttributeBuilder;
import poussecafe.attribute.optional.OptionalAttributeBuilder;
import poussecafe.attribute.set.SetAttributeBuilder;
import poussecafe.attribute.single.SingleAttributeBuilder;
import poussecafe.attribute.single.SingleAttributeBuilder.ExpectingAdaptedReader;
import poussecafe.util.StringId;

import static java.util.Objects.requireNonNull;

public class AttributeBuilder {

    private AttributeBuilder() {

    }

    public static <T> SingleAttributeBuilder.ExpectingReaderOrAdapter<T> single(Class<T> valueClass) {
        return new SingleAttributeBuilder<T>().usingValue(valueClass);
    }

    public static <T> ListAttributeBuilder<T> list(Class<T> elementClass) {
        return new ListAttributeBuilder<>(elementClass);
    }

    public static <T> SetAttributeBuilder<T> set(Class<T> elementClass) {
        return new SetAttributeBuilder<>(elementClass);
    }

    public static <K, V> MapAttributeBuilder<K, V> map(Class<K> keyClass, Class<V> valueClass) {
        Objects.requireNonNull(keyClass);
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
        return new SingleAttributeBuilder<T>().usingDataAdapter(dataAdapter);
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
