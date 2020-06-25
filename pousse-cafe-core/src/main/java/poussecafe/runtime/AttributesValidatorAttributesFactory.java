package poussecafe.runtime;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.OptionalAttribute;

public class AttributesValidatorAttributesFactory {

    public static Map<String, ValidationType> build(Class<?> attributesDefinition) {
        var attributes = new HashMap<String, ValidationType>();
        for(Method method : attributesDefinition.getMethods()) {
            Class<?> returnType = method.getReturnType();
            if(OptionalAttribute.class.isAssignableFrom(returnType)) {
                attributes.put(method.getName(), ValidationType.PRESENT);
            } else if(Attribute.class.isAssignableFrom(returnType)) {
                attributes.put(method.getName(), ValidationType.NOT_NULL);
            }
        }
        return attributes;
    }

    private AttributesValidatorAttributesFactory() {

    }
}
