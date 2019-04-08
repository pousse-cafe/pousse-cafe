package poussecafe.attribute.adapters;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import poussecafe.attribute.AutoAdaptingDataAdapter;
import poussecafe.exception.PousseCafeException;
import poussecafe.util.StringId;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class DataAdapters {

    private DataAdapters() {

    }

    public static DataAdapter<String, LocalDate> stringLocalDate() {
        return new DataAdapter<String, LocalDate>() {
            @Override
            public LocalDate adaptGet(String storedValue) {
                return LocalDate.parse(storedValue);
            }

            @Override
            public String adaptSet(LocalDate valueToStore) {
                return valueToStore.toString();
            }
        };
    }

    public static DataAdapter<String, LocalDateTime> stringLocalDateTime() {
        return new DataAdapter<String, LocalDateTime>() {
            @Override
            public LocalDateTime adaptGet(String storedValue) {
                return LocalDateTime.parse(storedValue);
            }

            @Override
            public String adaptSet(LocalDateTime valueToStore) {
                return valueToStore.toString();
            }
        };
    }

    public static DataAdapter<String, OffsetDateTime> stringOffsetDateTime() {
        return new DataAdapter<String, OffsetDateTime>() {
            @Override
            public OffsetDateTime adaptGet(String storedValue) {
                return OffsetDateTime.parse(storedValue);
            }

            @Override
            public String adaptSet(OffsetDateTime valueToStore) {
                return valueToStore.toString();
            }
        };
    }

    public static DataAdapter<String, BigDecimal> stringBigDecimal() {
        return new DataAdapter<String, BigDecimal>() {
            @Override
            public BigDecimal adaptGet(String storedValue) {
                return new BigDecimal(storedValue);
            }

            @Override
            public String adaptSet(BigDecimal valueToStore) {
                return valueToStore.toString();
            }
        };
    }

    public static <T extends StringId> DataAdapter<String, T> stringId(Class<T> stringIdClass) {
        Objects.requireNonNull(stringIdClass);
        return new DataAdapter<String, T>() {
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
        return new DataAdapter<String, E>() {
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
        return new DataAdapter<T, T>() {
            @Override
            public T adaptGet(T storedValue) {
                return storedValue;
            }

            @Override
            public T adaptSet(T valueToStore) {
                return valueToStore;
            }
        };
    }

    public static <U, T> DataAdapter<List<U>, List<T>> listWithAdapter(DataAdapter<U, T> adapter) {
        return new DataAdapter<List<U>, List<T>>() {
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
        return (Class<List<T>>) emptyList().getClass();
    }
}
