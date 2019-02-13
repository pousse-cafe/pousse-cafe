package poussecafe.context;

import java.util.List;
import java.util.Objects;
import poussecafe.messaging.MessageListener;

public class MessageListenersDiscoverer {

    public List<MessageListener> discoverDeclaredListeners(Object service) {
        Objects.requireNonNull(service);
        MessageListenerDiscoverer explorer = new MessageListenerDiscoverer.Builder()
                .service(service)
                .messageListenerFactory(new DeclaredMessageListenerFactory())
                .build();
        return explorer.discoverListeners();
    }
}
