package poussecafe.shop.domain;

import java.util.List;
import poussecafe.domain.Repository;

public class MessageRepository extends Repository<Message, MessageId, Message.Attributes> {

    public List<Message> findByCustomer(CustomerId customerId) {
        return wrap(dataAccess().findByCustomer(customerId));
    }

    @Override
    public MessageDataAccess<Message.Attributes> dataAccess() {
        return (MessageDataAccess<Message.Attributes>) super.dataAccess();
    }
}
