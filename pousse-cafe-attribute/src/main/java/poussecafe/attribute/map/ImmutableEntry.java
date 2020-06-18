package poussecafe.attribute.map;

import java.util.Map.Entry;
import java.util.Objects;

import static poussecafe.util.Equality.bothNullOrEqual;

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

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Entry) {
            Entry e = (Entry) obj;
            return bothNullOrEqual(key, e.getKey()) && bothNullOrEqual(value, e.getValue());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append("{key=");
        builder.append(key);
        builder.append(", value=");
        builder.append("}");
        return builder.toString();
    }
}