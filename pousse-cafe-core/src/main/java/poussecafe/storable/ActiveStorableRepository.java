package poussecafe.storable;

public abstract class ActiveStorableRepository<A extends ActiveStorable<K, D>, K, D extends IdentifiedStorableData<K>>
        extends IdentifiedStorableRepository<A, K, D> {

    @Override
    protected void addData(A storable) {
        MessageCollection messageCollection = storable.messageCollection();
        super.addData(storable);
        considerMessageSendingAfterAdd(storable, messageCollection);
    }

    protected void considerMessageSendingAfterAdd(A storable,
            MessageCollection messageCollection) {
        storable.storage().getMessageSendingPolicy().considerSending(messageCollection);
    }

    @Override
    protected void updateData(A storable) {
        MessageCollection messageCollection = storable.messageCollection();
        super.updateData(storable);
        considerMessageSendingAfterUpdate(storable, messageCollection);
    }

    protected void considerMessageSendingAfterUpdate(A storable,
            MessageCollection messageCollection) {
        storable.storage().getMessageSendingPolicy().considerSending(messageCollection);
    }

    @Override
    protected void deleteData(A storable) {
        MessageCollection messageCollection = storable.messageCollection();
        super.deleteData(storable);
        considerMessageSendingAfterDelete(storable, messageCollection);
    }

    protected void considerMessageSendingAfterDelete(A storable,
            MessageCollection messageCollection) {
        storable.storage().getMessageSendingPolicy().considerSending(messageCollection);
    }

}
