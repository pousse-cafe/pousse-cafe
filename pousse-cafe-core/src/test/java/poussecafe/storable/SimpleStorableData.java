package poussecafe.storable;

import poussecafe.inmemory.InlineProperty;

public class SimpleStorableData implements SimpleStorable.Data {

    @Override
    public Property<String> key() {
        return key;
    }

    private InlineProperty<String> key;
}
