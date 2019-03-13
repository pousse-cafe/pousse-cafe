package poussecafe.messaging;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class MessageListener {

    public static class Builder {

        private MessageListener listener = new MessageListener();

        public Builder id(String id) {
            listener.id = id;
            return this;
        }

        public Builder messageClass(Class<? extends Message> messageClass) {
            listener.messageClass = messageClass;
            return this;
        }

        public Builder consumer(Consumer<Message> consumer) {
            listener.consumer = consumer;
            return this;
        }

        @SuppressWarnings("rawtypes")
        public Builder runner(Optional<AggregateMessageListenerRunner> runner) {
            listener.runner = runner;
            return this;
        }

        public MessageListener build() {
            Objects.requireNonNull(listener.id);
            Objects.requireNonNull(listener.messageClass);
            Objects.requireNonNull(listener.consumer);
            Objects.requireNonNull(listener.runner);
            return listener;
        }
    }

    private MessageListener() {

    }

    private String id;

    public String id() {
        return id;
    }

    private Class<? extends Message> messageClass;

    public Class<? extends Message> messageClass() {
        return messageClass;
    }

    private Consumer<Message> consumer;

    public Consumer<Message> consumer() {
        return consumer;
    }

    @SuppressWarnings("rawtypes")
    private Optional<AggregateMessageListenerRunner> runner = Optional.empty();

    @SuppressWarnings("rawtypes")
    public Optional<AggregateMessageListenerRunner> runner() {
        return runner;
    }
}
