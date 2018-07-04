package poussecafe.util;

import static poussecafe.check.Checks.checkThatValue;

public class ReadOnlyEntry<T, S> implements java.util.Map.Entry<T, S> {

    public ReadOnlyEntry(T key, S value) {
        checkThatValue(key).notNull();
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
        throw new UnsupportedOperationException();
    }
}