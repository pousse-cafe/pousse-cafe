package poussecafe.storage.memory;

import java.io.Serializable;
import poussecafe.storable.ActiveStorableData;
import poussecafe.storable.MessageCollection;
import poussecafe.storable.Primitive;
import poussecafe.storable.PrimitiveSpecification;
import poussecafe.storage.Storage;

@SuppressWarnings("serial")
public abstract class InMemoryActiveData<K> extends Primitive implements ActiveStorableData<K>, Serializable {

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
