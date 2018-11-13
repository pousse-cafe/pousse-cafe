package poussecafe.property.accessor;

import poussecafe.property.Property;
import poussecafe.property.PropertyContainer;

public class PropertyContainerInAnotherPackage implements PropertyContainer {

    @Override
    public Property<String> property() {
        return new Property<String>() {
            @Override
            public String get() {
                return data;
            }

            @Override
            public void set(String value) {
                data = value;
            }
        };
    }

    private String data;
}
