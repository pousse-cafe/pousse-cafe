package poussecafe.sample.domain;

import java.util.List;
import poussecafe.domain.Repository;
import poussecafe.storable.MessageCollection;

public class MessageRepository extends Repository<Message, MessageKey, Message.Data> {

    @Override
    protected Message newAggregate() {
        return new Message();
    }

    @Override
    protected void considerMessageSendingAfterAdd(Message message,
            MessageCollection messageCollection) {
        messageCollection.addMessage(new MessageCreated(message.getKey()));
        super.considerMessageSendingAfterAdd(message, messageCollection);
    }

    public List<Message> findByCustomer(CustomerKey customerKey) {
        return newStorablesWithData(dataAccess().findByCustomer(customerKey));
    }

    private MessageDataAccess dataAccess() {
        return (MessageDataAccess) dataAccess;
    }

}
