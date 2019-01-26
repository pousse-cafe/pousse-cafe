package poussecafe.storage;

import java.util.ArrayList;
import java.util.List;
import poussecafe.domain.MessageCollection;
import poussecafe.messaging.Message;

import static java.util.Collections.unmodifiableList;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class DefaultMessageCollection implements MessageCollection {

    private List<Message> messages = new ArrayList<>();

    @Override
    public void addMessage(Message event) {
        checkThat(value(event).notNull().because("Message cannot be null"));
        messages.add(event);
    }

    @Override
    public List<Message> getMessages() {
        return unmodifiableList(messages);
    }

}
