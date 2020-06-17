package poussecafe.attribute.adapters;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import poussecafe.exception.PousseCafeException;
import poussecafe.util.StringId;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class DataAdapters {

    private DataAdapters() {

    }

    public static <U, T> DataAdapter<U, T> adapter(Function<U, T> adaptGet, Function<T, U> adaptSet) {
        return new DataAdapter<>() {
            @Override
            public T adaptGet(U storedValue) {
                return adaptGet.apply(storedValue);
            }

            @Override
            public U adaptSet(T valueToStore) {
                return adaptSet.apply(valueToStore);
            }
        };
    }

    public static DataAdapter<String, LocalDate> stringLocalDate() {
        return adapter(LocalDate::parse, LocalDate::toString);
    }

    public static DataAdapter<String, LocalDateTime> stringLocalDateTime() {
        return adapter(LocalDateTime::parse, LocalDateTime::toString);
    }

    public static DataAdapter<String, OffsetDateTime> stringOffsetDateTime() {
        return adapter(OffsetDateTime::parse, OffsetDateTime::toString);
    }

    public static DataAdapter<String, BigDecimal> stringBigDecimal() {
        return adapter(BigDecimal::new, BigDecimal::toString);
    }

    public static <T extends StringId> DataAdapter<String, T> stringId(Class<T> stringIdClass) {
        Objects.requireNonNull(stringIdClass);
        return new DataAdapter<>() {
            @Override
            public T adaptGet(String storedValue) {
                try {
                    return stringIdClass.getConstructor(String.class).newInstance(storedValue);
                } catch (Exception e) {
                    throw new PousseCafeException("Unable to adapt string id", e);
                }
            }

            @Override
            public String adaptSet(T valueToStore) {
                return valueToStore.stringValue();
            }
        };
    }

    public static <E extends Enum<E>> DataAdapter<String, E> stringEnum(Class<E> enumClass) {
        Objects.requireNonNull(enumClass);
        return new DataAdapter<>() {
            @Override
            public E adaptGet(String storedValue) {
                return Enum.valueOf(enumClass, storedValue);
            }

            @Override
            public String adaptSet(E valueToStore) {
                return valueToStore.name();
            }
        };
    }

    public static <U, T> DataAdapter<U, T> auto(Class<T> propertyTypeClass, Class<U> dataAdapterClass) {
        return new AutoAdaptingDataAdapter<>(propertyTypeClass, dataAdapterClass);
    }

    public static <T> DataAdapter<T, T>  identity() {
        return adapter(value -> value, value -> value);
    }

    public static <U, T> DataAdapter<List<U>, List<T>> listWithAdapter(DataAdapter<U, T> adapter) {
        return new DataAdapter<>() {
            @Override
            public List<T> adaptGet(List<U> storedValue) {
                return storedValue.stream().map(adapter::adaptGet).collect(toList());
            }

            @Override
            public List<U> adaptSet(List<T> valueToStore) {
                return valueToStore.stream().map(adapter::adaptSet).collect(toList());
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<List<T>> parametrizedListClass(Class<T> elementType) {
        requireNonNull(elementType);
        return (Class<List<T>>) emptyList().getClass();
    }

    public static DataAdapter<String, byte[]> stringByteArray() {
        return new DataAdapter<>() {
            @Override
            public byte[] adaptGet(String storedValue) {
                return Base64.getDecoder().decode(storedValue);
            }

            @Override
            public String adaptSet(byte[] valueToStore) {
                return Base64.getEncoder().encodeToString(valueToStore);
            }

        };
    }

    public static <L, U, K, V> DataAdapter<Entry<L, U>, Entry<K, V>> mutableEntry(
            DataAdapter<L, K> keyAdapter, DataAdapter<U, V> valueAdapter) {
        return new DataAdapter<>() {
            @Override
            public Entry<K, V> adaptGet(Entry<L, U> storedValue) {
                return new AdaptingMapEntry.Builder<L, U, K, V>()
                        .entry(storedValue)
                        .keyAdapter(keyAdapter)
                        .valueAdapter(valueAdapter)
                        .build();
            }

            @SuppressWarnings({ "rawtypes", "unchecked" })
            @Override
            public Entry<L, U> adaptSet(Entry<K, V> valueToStore) {
                if(valueToStore instanceof AdaptingMapEntry) {
                    AdaptingMapEntry adaptingEntry = (AdaptingMapEntry) valueToStore;
                    return adaptingEntry.underlyingEntry();
                }
                return null;
            }
        };
    }
}
