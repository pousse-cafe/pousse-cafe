package poussecafe.storable;

public abstract class ActiveStorable<K, D extends StorableData<K>> extends Storable<K, D> {

    private MessageCollection messageCollection;

    void setMessageCollection(MessageCollection messageCollection) {
        this.messageCollection = messageCollection;
    }

    protected MessageCollection getMessageCollection() {
        return messageCollection;
    }
}
