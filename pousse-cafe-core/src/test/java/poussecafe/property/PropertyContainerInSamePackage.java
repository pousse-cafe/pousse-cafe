package poussecafe.property;

public class PropertyContainerInSamePackage implements PropertyContainer {

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
