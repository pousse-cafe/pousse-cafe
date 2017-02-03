package poussecafe.sample.domain;

import poussecafe.domain.Factory;
import poussecafe.util.IdGenerator;

public class MessageFactory extends Factory<MessageKey, Message, Message.Data> {

    private IdGenerator idGenerator;

    @Override
    protected Message newAggregate() {
        return new Message();
    }

    public Message buildMessage(CustomerKey customerKey) {
        Message message = newAggregateWithKey(new MessageKey(idGenerator.generateId()));
        message.setCustomerKey(customerKey);
        return message;
    }

    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
}
