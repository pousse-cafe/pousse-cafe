package poussecafe.attribute;

import poussecafe.attribute.Attribute;

public class AttributeContainerInSamePackage implements AttributeContainer {

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
