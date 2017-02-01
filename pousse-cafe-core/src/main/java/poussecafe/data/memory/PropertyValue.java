package poussecafe.data.memory;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class PropertyValue {

    private Property property;

    private Object value;

    public PropertyValue(Property property, Object defaultValue) {
        setProperty(property);
        set(defaultValue);
    }

    private void setProperty(Property property) {
        checkThat(value(property).notNull().because("Property cannot be null"));
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }

    public void set(Object value) {
        this.value = value;
    }

    public Object get() {
        return value;
    }

    @Override
    public String toString() {
        return "PropertyValue [property=" + property + ", value=" + value + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((property == null) ? 0 : property.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        PropertyValue other = (PropertyValue) obj;
        if (property == null) {
            if (other.property != null) {
                return false;
            }
        } else if (!property.equals(other.property)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }
}
