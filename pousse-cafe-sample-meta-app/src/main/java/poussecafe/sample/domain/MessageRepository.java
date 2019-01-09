package poussecafe.sample.domain;

import java.util.List;
import poussecafe.domain.Repository;

public class MessageRepository extends Repository<Message, MessageKey, Message.Data> {

    public List<Message> findByCustomer(CustomerKey customerKey) {
        return newEntitiesWithData(dataAccess().findByCustomer(customerKey));
    }

    private MessageDataAccess<Message.Data> dataAccess() {
        return (MessageDataAccess<Message.Data>) dataAccess;
    }
}
