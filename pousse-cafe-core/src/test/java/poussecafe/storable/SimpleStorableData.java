package poussecafe.storable;

public class SimpleStorableData implements SimpleStorable.Data {

    @Override
    public Property<SimpleStorableKey> key() {
        return new Property<SimpleStorableKey>() {
            @Override
            public SimpleStorableKey get() {
                return new SimpleStorableKey(key);
            }

            @Override
            public void set(SimpleStorableKey value) {
                key = value.getId();
            }
        };
    }

    private String key;
}
