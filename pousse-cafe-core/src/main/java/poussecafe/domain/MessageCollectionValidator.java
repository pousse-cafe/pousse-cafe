package poussecafe.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import poussecafe.messaging.Message;
import poussecafe.storage.MessageCollection;

public class MessageCollectionValidator {

    public void validate(MessageCollection messageCollection) {
        List<Message> messages = messageCollection.getMessages();
        Set<Message> messagesSet = new HashSet<>(messages);
        if(messages.size() != messagesSet.size()) {
            throw new IllegalArgumentException("The same message has been emitted more than once");
        }
    }
}
