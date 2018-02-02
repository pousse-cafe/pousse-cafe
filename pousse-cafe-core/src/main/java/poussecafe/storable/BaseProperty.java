package poussecafe.storable;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Collections.emptyList;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class BaseProperty<T> implements Property<T> {

    public BaseProperty(Class<? extends T> valueClass) {
        setValueClass(valueClass);
    }

    protected BaseProperty() {

    }

    private void setValueClass(Class<? extends T> valueClass) {
        checkThat(value(valueClass).notNull());
        this.valueClass = valueClass;
    }

    protected Class<? extends T> valueClass;

    @Override
    public Class<? extends T> getValueClass() {
        return valueClass;
    }

    @Override
    public T get() {
        T value = getValue();
        if(value == null && valueClass != null) {
            return defaultValue();
        } else {
            return value;
        }
    }

    protected abstract T getValue();

    @SuppressWarnings("unchecked")
    protected T defaultValue() {
        if(ArrayList.class.isAssignableFrom(valueClass)) {
            return (T) new ArrayList<>();
        } else if(Collection.class.isAssignableFrom(valueClass)) {
            return (T) emptyList();
        } else if(Integer.class.isAssignableFrom(valueClass)) {
            return (T) new Integer(0);
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
