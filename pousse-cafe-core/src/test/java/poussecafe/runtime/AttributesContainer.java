package poussecafe.runtime;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.attribute.OptionalAttribute;

public class AttributesContainer implements AttributesDefinition {

    @Override
    public Attribute<String> string() {
        return AttributeBuilder.single(String.class)
                .read(() -> string)
                .write(value -> string = value)
                .build();
    }

    private String string;

    @Override
    public OptionalAttribute<String> optionalString() {
        return AttributeBuilder.optional(String.class)
                .read(() -> optionalString)
                .write(value -> optionalString = value)
                .build();
    }

    private String optionalString;

    @Override
    public Attribute<String> dontCare() {
        return null; // This should not pass validation unless NONE ValidationType is used
    }
}
