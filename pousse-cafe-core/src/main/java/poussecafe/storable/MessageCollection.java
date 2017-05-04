package poussecafe.storable;

import java.util.List;
import poussecafe.messaging.Message;

public interface MessageCollection {

    void addMessage(Message message);

    List<Message> getMessages();
}
