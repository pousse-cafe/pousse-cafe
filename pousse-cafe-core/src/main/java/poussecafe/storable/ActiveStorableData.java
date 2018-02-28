package poussecafe.storable;

import poussecafe.storage.Storage;

public interface ActiveStorableData<K> extends IdentifiedStorableData<K> {

    MessageCollection messageCollection();

    void messageCollection(MessageCollection messageCollection);

    Storage storage();

    void storage(Storage storage);
}
