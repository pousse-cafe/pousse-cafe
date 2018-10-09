package poussecafe.sample.domain;

import poussecafe.domain.Factory;
import poussecafe.util.IdGenerator;

public class MessageFactory extends Factory<MessageKey, Message, Message.Data> {

    public Message buildMessage(CustomerKey customerKey) {
        Message message = newAggregateWithKey(new MessageKey(idGenerator.generateId()));
        message.setCustomerKey(customerKey);
        return message;
    }

    private IdGenerator idGenerator;
}
