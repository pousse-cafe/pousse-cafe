package poussecafe.processing;

import java.util.Collection;
import poussecafe.environment.MessageListener;
import poussecafe.messaging.Message;

public interface ListenersSet {

    Collection<MessageListener> messageListeners();

    Collection<MessageListener> messageListenersOf(Class<? extends Message> messageClass);

    boolean contains(MessageListener listener);
}
