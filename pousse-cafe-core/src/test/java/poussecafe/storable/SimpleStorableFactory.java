package poussecafe.storable;

public class SimpleStorableFactory
extends ActiveStorableFactory<SimpleStorableKey, SimpleStorable, SimpleStorable.Data> {

    public SimpleStorableFactory(StorableDataFactory<SimpleStorable.Data> storableDataFactory) {
        setStorableDataFactory(storableDataFactory);
    }

    @Override
    protected SimpleStorable newStorable() {
        return new SimpleStorable();
    }
}
