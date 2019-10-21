package poussecafe.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageSender;
import poussecafe.runtime.MessageSenderLocator;

public abstract class MessageSendingPolicy {

    private MessageSenderLocator messageSenderLocator;

    public abstract void considerSending(MessageCollection collection);

    public abstract MessageCollection newMessageCollection();

    protected void sendCollection(MessageCollection collection) {
        Map<MessageSender, List<Message>> messagesPerSender = messagesPerSender(collection);
        for(Entry<MessageSender, List<Message>> entry : messagesPerSender.entrySet()) {
            entry.getKey().sendMessages(entry.getValue());
        }
    }

    private Map<MessageSender, List<Message>> messagesPerSender(MessageCollection collection) {
        Map<MessageSender, List<Message>> messagesPerSender = new HashMap<>();
        for (Message message : collection.getMessages()) {
            MessageSender sender = messageSenderLocator.locate(message.getClass());
            List<Message> senderMessages = messagesPerSender.computeIfAbsent(sender, key -> new ArrayList<>());
            senderMessages.add(message);
        }
        return messagesPerSender;
    }

    public void setMessageSenderLocator(MessageSenderLocator messageSenderLocator) {
        Objects.requireNonNull(messageSenderLocator);
        this.messageSenderLocator = messageSenderLocator;
    }

}
