package poussecafe.messaging;

import poussecafe.environment.MessageListenerRegistry;

public interface MessageReceiverFactory {

    MessageReceiver build(MessageListenerRegistry listenerRegistry);
}
