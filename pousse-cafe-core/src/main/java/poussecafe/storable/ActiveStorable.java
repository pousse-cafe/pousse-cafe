package poussecafe.storable;

import poussecafe.storage.Storage;

public abstract class ActiveStorable<K, D extends IdentifiedStorableData<K>> extends IdentifiedStorable<K, D> {

    public MessageCollection messageCollection() {
        return messageCollection;
    }

    private MessageCollection messageCollection;

    public void messageCollection(MessageCollection messageCollection) {
        this.messageCollection = messageCollection;
    }

    public Storage storage() {
        return storage;
    }

    private Storage storage;

    public void storage(Storage storage) {
        this.storage = storage;
    }

    @Override
    protected <T> T newPrimitive(PrimitiveSpecification<T> specification) {
        T newPrimitive = super.newPrimitive(specification);
        if(newPrimitive instanceof ActiveStorable) {
            @SuppressWarnings("rawtypes")
            ActiveStorable activeStorable = (ActiveStorable) newPrimitive;
            activeStorable.storage(storage);
            activeStorable.messageCollection(messageCollection);
        }
        return newPrimitive;
    }
}
