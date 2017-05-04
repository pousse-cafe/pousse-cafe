package poussecafe.storable;

import poussecafe.storage.MessageSendingPolicy;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class ActiveStorableRepository<A extends ActiveStorable<K, D>, K, D extends StorableData<K>>
extends StorableRepository<A, K, D> {

    protected MessageSendingPolicy messageSendingPolicy;

    public void setMessageSendingPolicy(MessageSendingPolicy messageSendingPolicy) {
        checkThat(value(messageSendingPolicy)
                .notNull()
                .because("Message sending policy cannot be null"));
        this.messageSendingPolicy = messageSendingPolicy;
    }

    @Override
    protected A newStorableWithData(D data) {
        A storable = super.newStorableWithData(data);
        storable.setMessageCollection(messageSendingPolicy.newMessageCollection());
        return storable;
    }

    @Override
    protected void addData(A storable) {
        MessageCollection messageCollection = storable.getMessageCollection();
        super.addData(storable);
        considerMessageSendingAfterAdd(storable, messageCollection);
    }

    protected void considerMessageSendingAfterAdd(A storable,
            MessageCollection messageCollection) {
        messageSendingPolicy.considerSending(messageCollection);
    }

    @Override
    protected void updateData(A storable) {
        MessageCollection messageCollection = storable.getMessageCollection();
        super.updateData(storable);
        considerMessageSendingAfterUpdate(storable, messageCollection);
    }

    protected void considerMessageSendingAfterUpdate(A storable,
            MessageCollection messageCollection) {
        messageSendingPolicy.considerSending(messageCollection);
    }

    @Override
    protected void deleteData(A storable) {
        MessageCollection messageCollection = storable.getMessageCollection();
        super.deleteData(storable);
        considerMessageSendingAfterDelete(storable, messageCollection);
    }

    protected void considerMessageSendingAfterDelete(A storable,
            MessageCollection messageCollection) {
        messageSendingPolicy.considerSending(messageCollection);
    }

}
