package poussecafe.data.memory;

import poussecafe.exception.AssertionFailedException;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.emptyOrNullString;
import static poussecafe.check.Predicates.not;

public class Property {

    private String name;

    private Class<?> type;

    private String setter;

    private String getter;

    public Property(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        checkThat(value(name).verifies(not(emptyOrNullString())).because("Property name cannot be empty"));
        this.name = name;
    }

    public String getSetter() {
        return setter;
    }

    public void setSetter(String setter) {
        checkThat(value(setter).verifies(not(emptyOrNullString())).because("Setter cannot be empty"));
        this.setter = setter;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        checkThat(value(getter).verifies(not(emptyOrNullString())).because("Getter cannot be empty"));
        this.getter = getter;
    }

    public boolean hasSetter() {
        return setter != null && !setter.isEmpty();
    }

    public boolean hasGetter() {
        return getter != null && !getter.isEmpty();
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        checkThat(value(type).notNull().because("Type cannot be empty"));
        if (this.type != null && !this.type.equals(type)) {
            throw new AssertionFailedException("Type conflict on property " + name);
        }
        this.type = type;
    }

    @Override
    public String toString() {
        return "Property [name=" + name + ", type=" + type + ", setter=" + setter + ", getter=" + getter + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getter == null) ? 0 : getter.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((setter == null) ? 0 : setter.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Property other = (Property) obj;
        if (getter == null) {
            if (other.getter != null) {
                return false;
            }
        } else if (!getter.equals(other.getter)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (setter == null) {
            if (other.setter != null) {
                return false;
            }
        } else if (!setter.equals(other.setter)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

}
