package poussecafe.runtime;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.OptionalAttribute;

public interface AttributesDefinition {

    Attribute<String> string();

    OptionalAttribute<String> optionalString();

    Attribute<String> dontCare();
}
