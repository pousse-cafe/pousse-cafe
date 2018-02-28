package poussecafe.storable;

public abstract class ActiveStorableRepository<A extends ActiveStorable<K, D>, K, D extends ActiveStorableData<K>>
        extends IdentifiedStorableRepository<A, K, D> {

    @Override
    protected void addData(A storable) {
        MessageCollection messageCollection = storable.getData().messageCollection();
        super.addData(storable);
        considerMessageSendingAfterAdd(storable, messageCollection);
    }

    protected void considerMessageSendingAfterAdd(A storable,
            MessageCollection messageCollection) {
        storable.getData().storage().getMessageSendingPolicy().considerSending(messageCollection);
    }

    @Override
    protected void updateData(A storable) {
        MessageCollection messageCollection = storable.getData().messageCollection();
        super.updateData(storable);
        considerMessageSendingAfterUpdate(storable, messageCollection);
    }

    protected void considerMessageSendingAfterUpdate(A storable,
            MessageCollection messageCollection) {
        storable.getData().storage().getMessageSendingPolicy().considerSending(messageCollection);
    }

    @Override
    protected void deleteData(A storable) {
        MessageCollection messageCollection = storable.getData().messageCollection();
        super.deleteData(storable);
        considerMessageSendingAfterDelete(storable, messageCollection);
    }

    protected void considerMessageSendingAfterDelete(A storable,
            MessageCollection messageCollection) {
        storable.getData().storage().getMessageSendingPolicy().considerSending(messageCollection);
    }

}
