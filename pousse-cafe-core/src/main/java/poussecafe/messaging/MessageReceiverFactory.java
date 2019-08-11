package poussecafe.messaging;

import poussecafe.environment.MessageListenerRegistrar;

public interface MessageReceiverFactory {

    MessageReceiver build(MessageListenerRegistrar listenerRegistry);
}
