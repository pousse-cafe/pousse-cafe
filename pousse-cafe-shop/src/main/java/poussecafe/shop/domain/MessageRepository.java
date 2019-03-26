package poussecafe.shop.domain;

import java.util.List;
import poussecafe.domain.Repository;

public class MessageRepository extends Repository<Message, MessageKey, Message.Attributes> {

    public List<Message> findByCustomer(CustomerKey customerKey) {
        return wrap(dataAccess().findByCustomer(customerKey));
    }

    @Override
    public MessageDataAccess<Message.Attributes> dataAccess() {
        return (MessageDataAccess<Message.Attributes>) super.dataAccess();
    }
}
