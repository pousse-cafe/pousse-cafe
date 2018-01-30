package poussecafe.inmemory;

import java.util.Collection;
import poussecafe.storable.Property;

import static java.util.Collections.emptyList;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class BaseProperty<T> implements Property<T> {

    public BaseProperty(Class<T> valueClass) {
        setValueClass(valueClass);
    }

    private void setValueClass(Class<T> valueClass) {
        checkThat(value(valueClass).notNull());
        this.valueClass = valueClass;
    }

    private Class<T> valueClass;

    @Override
    public T get() {
        T value = getValue();
        if(value == null) {
            return defaultValue(valueClass);
        } else {
            return value;
        }
    }

    protected abstract T getValue();

    @SuppressWarnings("unchecked")
    private T defaultValue(Class<T> valueClass) {
        if(Collection.class.isAssignableFrom(valueClass)) {
            return (T) emptyList();
        } else {
            return null;
        }
    }

    @Override
    public void set(T value) {
        setValue(value);
    }

    protected abstract void setValue(T value);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        BaseProperty other = (BaseProperty) obj;
        if (getValue() == null) {
            if (other.getValue() != null) {
                return false;
            }
        } else if (!getValue().equals(other.getValue())) {
            return false;
        }
        return true;
    }
}
