package poussecafe.storable;

import poussecafe.storage.memory.InlineProperty;

public class SimpleStorableData implements SimpleStorable.Data {

    @Override
    public Property<SimpleStorableKey> key() {
        return new ConvertingProperty<String, SimpleStorableKey>(key) {
            @Override
            protected SimpleStorableKey convertFrom(String from) {
                return new SimpleStorableKey(from);
            }

            @Override
            protected String convertTo(SimpleStorableKey to) {
                return to.getId();
            }
        };
    }

    private InlineProperty<String> key = new InlineProperty<>(String.class);
}
