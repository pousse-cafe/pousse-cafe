package poussecafe.data.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class InMemoryDataImplementation {

    private Class<?> dataClass;

    private Set<Property> properties;

    private Map<String, PropertyValue> values;

    public InMemoryDataImplementation(Class<?> dataClass) {
        setDataClass(dataClass);
        initProperties();
        initSettersAndGetters();
    }

    private void setDataClass(Class<?> dataClass) {
        checkThat(value(dataClass).notNull().because("Class cannot be null"));
        this.dataClass = dataClass;
    }

    private void initProperties() {
        PropertyModelExtractor extractor = new PropertyModelExtractor(dataClass);
        extractor.scanClass();
        properties = extractor.buildPropertyModel();
    }

    private void initSettersAndGetters() {
        values = new HashMap<>();
        for (Property property : properties) {
            PropertyValue propertyValue = new PropertyValue(property, getDefaultValue(property.getType()));
            values.put(property.getName(), propertyValue);
        }
    }

    private Object getDefaultValue(Class<?> type) {
        if (Integer.TYPE.equals(type)) {
            return 0;
        }
        if (Double.TYPE.equals(type)) {
            return 0.;
        }
        if (Float.TYPE.equals(type)) {
            return 0.f;
        }
        if (Byte.TYPE.equals(type)) {
            return (byte) 0;
        }
        if (Short.TYPE.equals(type)) {
            return (short) 0;
        }
        if (Long.TYPE.equals(type)) {
            return 0L;
        }
        if (Character.TYPE.equals(type)) {
            return '\u0000';
        }
        if (Boolean.TYPE.equals(type)) {
            return false;
        }
        if (List.class.equals(type)) {
            return new ArrayList<>();
        }
        return null;
    }

    public void set(String propertyName,
            Object value) {
        getExistingPropertyValue(propertyName).set(value);
    }

    private PropertyValue getExistingPropertyValue(String propertyName) {
        PropertyValue propertyValue = values.get(propertyName);
        if (propertyValue == null) {
            throw new InMemoryDataException("No property with name " + propertyName);
        }
        return propertyValue;
    }

    public Object get(String propertyName) {
        return getExistingPropertyValue(propertyName).get();
    }

    @Override
    public String toString() {
        return "In-memory implementation of " + dataClass.getName() + "[" + propertyValuesToString() + "]";
    }

    private String propertyValuesToString() {
        return values.values().stream().map(PropertyValue::toString).collect(joining(","));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof InMemoryDataImplementation) {
            return equalsImplementation((InMemoryDataImplementation) obj);
        }
        try {
            return equalsImplementation(InMemoryDataUtils.getDataImplementation(obj));
        } catch (InMemoryDataException e) { // NOSONAR
            return false;
        }
    }

    private boolean equalsImplementation(InMemoryDataImplementation otherData) {
        return values.equals(otherData.values);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (PropertyValue value : values.values()) {
            hashCode += 31 * value.hashCode();
        }
        return hashCode;
    }

    public Set<Property> getProperties() {
        return properties;
    }
}
