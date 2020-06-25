package poussecafe.runtime;

import java.lang.reflect.Method;
import java.util.Optional;
import poussecafe.annotations.Validation;

public class AttributeMethod {

    public Optional<ValidationType> validationType() {
        return validationAnnotation.map(Validation::value);
    }

    private Optional<Validation> validationAnnotation;

    public Optional<String> fieldName() {
        String annotationField = validationAnnotation.map(Validation::field).orElse("");
        if(annotationField.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(annotationField);
        }
    }

    public static class Builder {

        private AttributeMethod attributeMethod = new AttributeMethod();

        public Builder containerClass(Class<?> containerClass) {
            this.containerClass = containerClass;
            return this;
        }

        private Class<?> containerClass;

        public Builder attributeName(String attributeName) {
            this.attributeName = attributeName;
            return this;
        }

        private String attributeName;

        public AttributeMethod build() {
            try {
                Method method = containerClass.getMethod(attributeName);
                Validation annotation = method.getAnnotation(Validation.class);
                attributeMethod.validationAnnotation = Optional.ofNullable(annotation);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new IllegalArgumentException("Attribute method " + attributeName + " not found");
            }
            return attributeMethod;
        }
    }
}
