package poussecafe.attribute.accessor;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeContainer;

public class AttributeContainerInAnotherPackage implements AttributeContainer {

    @Override
    public Attribute<String> property() {
        return new Attribute<String>() {
            @Override
            public String value() {
                return data;
            }

            @Override
            public void value(String value) {
                data = value;
            }
        };
    }

    private String data;
}
