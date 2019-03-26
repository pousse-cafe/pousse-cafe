package poussecafe.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import poussecafe.messaging.Message;

import static java.util.Collections.unmodifiableList;

public class DefaultMessageCollection implements MessageCollection {

    private List<Message> messages = new ArrayList<>();

    @Override
    public void addMessage(Message event) {
        Objects.requireNonNull(event);
        messages.add(event);
    }

    @Override
    public List<Message> getMessages() {
        return unmodifiableList(messages);
    }

}
