package poussecafe.data.memory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

class PropertyModelExtractor {

    private Class<?> dataClass;

    private PropertyMethodsNamingPolicy methodsNamingPolicy;

    private Map<String, Property> extractedProperties;

    public PropertyModelExtractor(Class<?> dataClass) {
        setDataClass(dataClass);
        initPropertyMethodsNamingPolicy();
        extractedProperties = new HashMap<>();
    }

    private void setDataClass(Class<?> dataClass) {
        checkThat(value(dataClass).notNull().because("Data class cannot be null"));
        this.dataClass = dataClass;
    }

    private void initPropertyMethodsNamingPolicy() {
        methodsNamingPolicy = new PropertyMethodsNamingPolicy();
    }

    public void scanClass() {
        for (Method method : dataClass.getMethods()) {
            tryDefineProperty(method);
        }
    }

    private void tryDefineProperty(Method method) {
        String methodName = method.getName();
        if (methodsNamingPolicy.isSetterOrGetter(methodName)) {
            String propertyName = methodsNamingPolicy.extractPropertyName(methodName);
            Property property = definePropertyIfNotAlready(propertyName);
            if (methodsNamingPolicy.isSetter(methodName)) {
                property.setSetter(methodName);
                property.setType(method.getParameters()[0].getType());
            }
            if (methodsNamingPolicy.isGetter(methodName)) {
                property.setGetter(methodName);
                property.setType(method.getReturnType());
            }
        }
    }

    private Property definePropertyIfNotAlready(String propertyName) {
        Property property;
        if (!extractedProperties.containsKey(propertyName)) {
            property = new Property(propertyName);
            extractedProperties.put(propertyName, property);
        } else {
            property = extractedProperties.get(propertyName);
        }
        return property;
    }

    public Set<Property> buildPropertyModel() {
        return new HashSet<>(extractedProperties.values());
    }
}
