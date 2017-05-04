package poussecafe.messaging;

import poussecafe.configuration.MessageSenderRegistry;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class MessageRouter {

    private QueueSelector selector;

    private MessageSenderRegistry registry;

    public void routeMessage(Message message) {
        Queue source = selector.selectSource(message);
        MessageSender emitter = registry.getMessageSender(source);
        emitter.sendMessage(message);
    }

    public void setQueueSelector(QueueSelector selector) {
        checkThat(value(selector).notNull().because("Queue selector cannot be null"));
        this.selector = selector;
    }

    public void setMessageSenderRegistry(MessageSenderRegistry registry) {
        checkThat(value(registry).notNull().because("Message sender registry cannot be null"));
        this.registry = registry;
    }
}
