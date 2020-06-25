package poussecafe.runtime;

import poussecafe.annotations.Validation;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.attribute.OptionalAttribute;

public class AttributesContainerWithOverriddenField implements AttributesDefinition {

    @Override
    @Validation(value = ValidationType.NOT_NULL, field = "renamed")
    public Attribute<String> string() {
        return AttributeBuilder.single(String.class)
                .read(() -> renamed)
                .write(value -> renamed = value)
                .build();
    }

    private String renamed;

    @Override
    public OptionalAttribute<String> optionalString() {
        return AttributeBuilder.optional(String.class)
                .read(() -> optionalString)
                .write(value -> optionalString = value)
                .build();
    }

    private String optionalString;

    @Override
    @Validation(ValidationType.NONE)
    public Attribute<String> dontCare() {
        return null; // This should not pass validation unless NONE ValidationType is used
    }
}
