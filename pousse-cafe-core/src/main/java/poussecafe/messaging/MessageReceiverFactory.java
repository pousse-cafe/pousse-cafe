package poussecafe.messaging;

public interface MessageReceiverFactory {

    MessageReceiver build(MessageListenerRegistry listenerRegistry);
}
