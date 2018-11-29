package poussecafe.sample.domain;

import java.util.UUID;
import poussecafe.domain.Factory;

public class MessageFactory extends Factory<MessageKey, Message, Message.Data> {

    public Message buildMessage(CustomerKey customerKey) {
        Message message = newAggregateWithKey(new MessageKey(UUID.randomUUID().toString()));
        message.setCustomerKey(customerKey);
        return message;
    }
}
