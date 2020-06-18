package poussecafe.attribute.map;

import java.util.Objects;

import static poussecafe.util.Equality.referenceEquals;

public class ImmutableEntry<T, S> implements java.util.Map.Entry<T, S> {

    public ImmutableEntry(T key, S value) {
        Objects.requireNonNull(key);
        this.key = key;
        this.value = value;
    }

    private T key;

    private S value;

    @Override
    public T getKey() {
        return key;
    }

    @Override
    public S getValue() {
        return value;
    }

    @Override
    public S setValue(S value) {
        throw new UnsupportedOperationException("setValue");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(this::safeEquals);
    }

    public boolean safeEquals(ImmutableEntry<T, S> entry) {
        return key.equals(entry.key)
                && ((value == null && entry.value == null)
                        || (value != null && value.equals(entry.value)));
    }
}