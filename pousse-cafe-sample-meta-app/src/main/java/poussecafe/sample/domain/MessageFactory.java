package poussecafe.sample.domain;

import poussecafe.domain.Factory;

public class MessageFactory extends Factory<MessageKey, Message, Message.Data> {

    @Override
    protected Message newAggregate() {
        return new Message();
    }

    public Message buildMessage(CustomerKey customerKey) {
        Message message = newAggregateWithKey(new MessageKey());
        message.setCustomerKey(customerKey);
        return message;
    }

}
