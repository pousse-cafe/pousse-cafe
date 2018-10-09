package poussecafe.sample.domain;

import java.util.List;
import poussecafe.domain.Repository;
import poussecafe.property.MessageCollection;

public class MessageRepository extends Repository<Message, MessageKey, Message.Data> {

    @Override
    protected void considerMessageSendingAfterAdd(Message message,
            MessageCollection messageCollection) {
        messageCollection.addMessage(new MessageCreated(message.getKey()));
        super.considerMessageSendingAfterAdd(message, messageCollection);
    }

    public List<Message> findByCustomer(CustomerKey customerKey) {
        return newEntitiesWithData(dataAccess().findByCustomer(customerKey));
    }

    private MessageDataAccess<Message.Data> dataAccess() {
        return (MessageDataAccess<Message.Data>) dataAccess;
    }

}
