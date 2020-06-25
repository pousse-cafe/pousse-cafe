package poussecafe.runtime;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import poussecafe.util.InstanceField;

import static poussecafe.util.ReflectionUtils.access;

public class AttributesValidator {

    public void validOrElseThrow(Object container) {
        var accessor = access(container);
        for(Entry<String, ValidationType> attribute : attributes.entrySet()) {
            var attributeName = attribute.getKey();
            var validationType = attribute.getValue();

            AttributeMethod attributeMethod = new AttributeMethod.Builder()
                    .containerClass(container.getClass())
                    .attributeName(attributeName)
                    .build();
            Optional<ValidationType> overridingValidationType = attributeMethod.validationType();
            Optional<String> fieldName = attributeMethod.fieldName();
            if(overridingValidationType.isPresent()) {
                validationType = overridingValidationType.get();
            }

            var field = accessor.instanceField(fieldName.orElse(attributeName));
            if(validationType == ValidationType.NONE) {
                // NoOp
            } else if(validationType == ValidationType.NOT_NULL) {
                requireNonNull(field, attributeName + " value cannot be null");
            } else if(validationType == ValidationType.PRESENT) {
                requirePresent(field, attributeName + " field not found");
            } else {
                throw new UnsupportedOperationException("Unsupported validation type " + validationType);
            }
        }
    }

    private Map<String, ValidationType> attributes;

    private void requireNonNull(InstanceField field, String errorMessage) {
        require(() -> field.isPresent() && field.get() != null, errorMessage);
    }

    private void require(BooleanSupplier predicate, String errorMessage) {
        if(!predicate.getAsBoolean()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void requirePresent(InstanceField field, String errorMessage) {
        require(field::isPresent, errorMessage);
    }

    public static class Builder {

        private AttributesValidator validator = new AttributesValidator();

        public AttributesValidator build() {
            Objects.requireNonNull(validator.attributes);
            return validator;
        }

        public Builder attributes(Map<String, ValidationType> attributes) {
            validator.attributes = attributes;
            return this;
        }

        public Builder definition(Class<?> attributesDefinition) {
            validator.attributes = AttributesValidatorAttributesFactory.build(attributesDefinition);
            return this;
        }
    }

    private AttributesValidator() {

    }
}
