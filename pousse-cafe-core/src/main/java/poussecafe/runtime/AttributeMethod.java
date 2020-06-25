package poussecafe.runtime;

import java.lang.reflect.Method;
import java.util.Optional;
import poussecafe.annotations.Validation;

public class AttributeMethod {

    public Optional<ValidationType> validationType() {
        Validation annotation = method.getAnnotation(Validation.class);
        return Optional.ofNullable(annotation).map(Validation::value);
    }

    private Method method;

    public static class Builder {

        private AttributeMethod method = new AttributeMethod();

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
                method.method = containerClass.getMethod(attributeName);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new IllegalArgumentException("Attribute method " + attributeName + " not found");
            }
            return method;
        }
    }
}
