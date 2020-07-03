package poussecafe.source.model;

import java.util.Optional;

public class MessageListenerContainer {

    public static MessageListenerContainer aggregateRoot(String name) {
        MessageListenerContainer container = new MessageListenerContainer();
        container.aggregateName = Optional.of(name);
        container.type = MessageListenerContainerType.ROOT;
        return container;
    }

    private MessageListenerContainer() {

    }

    public Optional<String> aggregateName() {
        return aggregateName;
    }

    private Optional<String> aggregateName;

    public MessageListenerContainerType type() {
        return type;
    }

    private MessageListenerContainerType type;

    public static MessageListenerContainer factory(String name) {
        MessageListenerContainer container = new MessageListenerContainer();
        container.aggregateName = Optional.of(name);
        container.type = MessageListenerContainerType.FACTORY;
        return container;
    }

    public static MessageListenerContainer repository(String name) {
        MessageListenerContainer container = new MessageListenerContainer();
        container.aggregateName = Optional.of(name);
        container.type = MessageListenerContainerType.REPOSITORY;
        return container;
    }
}
