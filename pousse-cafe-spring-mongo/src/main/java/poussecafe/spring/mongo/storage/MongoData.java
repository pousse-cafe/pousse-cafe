package poussecafe.spring.mongo.storage;

import poussecafe.storable.ActiveStorableData;
import poussecafe.storable.MessageCollection;
import poussecafe.storable.Primitive;
import poussecafe.storable.PrimitiveSpecification;
import poussecafe.storage.Storage;

public abstract class MongoData<K> extends Primitive implements ActiveStorableData<K> {

    @Override
    public MessageCollection messageCollection() {
        return messageCollection;
    }

    private transient MessageCollection messageCollection;

    @Override
    public void messageCollection(MessageCollection messageCollection) {
        this.messageCollection = messageCollection;
    }

    @Override
    public Storage storage() {
        return storage;
    }

    private transient Storage storage;

    @Override
    public void storage(Storage storage) {
        this.storage = storage;
    }

    @Override
    protected <T> T newPrimitive(PrimitiveSpecification<T> specification) {
        T newPrimitive = super.newPrimitive(specification);
        if(newPrimitive instanceof ActiveStorableData) {
            @SuppressWarnings("rawtypes")
            ActiveStorableData activeStorableData = (ActiveStorableData) newPrimitive;
            activeStorableData.storage(storage);
            activeStorableData.messageCollection(messageCollection);
        }
        return newPrimitive;
    }

}
