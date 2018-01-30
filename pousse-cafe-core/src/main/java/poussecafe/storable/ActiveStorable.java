package poussecafe.storable;

import poussecafe.storage.Storage;

public abstract class ActiveStorable<K, D extends StorableData> extends IdentifiedStorable<K, D> {

    private MessageCollection messageCollection;

    protected MessageCollection getMessageCollection() {
        return messageCollection;
    }

    void setStorage(Storage storage) {
        this.storage = storage;
        this.messageCollection = storage.getMessageSendingPolicy().newMessageCollection();
    }

    Storage storage;

    protected Storage getStorage() {
        return storage;
    }
}
