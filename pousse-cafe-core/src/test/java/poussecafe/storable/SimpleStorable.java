package poussecafe.storable;

public class SimpleStorable extends ActiveStorable<SimpleStorableKey, SimpleStorable.Data> {

    @Override
    public SimpleStorableKey getKey() {
        return new SimpleStorableKey(getData().key().get());
    }

    @Override
    public void setKey(SimpleStorableKey key) {
        getData().key().set(key.getId());
    }

    public static interface Data extends StorableData {

        Property<String> key();
    }
}
