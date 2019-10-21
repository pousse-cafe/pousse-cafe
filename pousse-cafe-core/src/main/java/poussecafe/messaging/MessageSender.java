package poussecafe.messaging;

import java.util.List;
import poussecafe.runtime.OriginalAndMarshaledMessage;

import static java.util.stream.Collectors.toList;

public abstract class MessageSender {

    protected MessageSender(MessageAdapter messageAdapter) {
        this.messageAdapter = messageAdapter;
    }

    public void sendMessage(Message message) {
        OriginalAndMarshaledMessage marshalledMessage = marshal(message);
        sendMarshalledMessage(marshalledMessage);
    }

    private OriginalAndMarshaledMessage marshal(Message message) {
        Object marshalledMessage = messageAdapter.adaptMessage(message);
        return new OriginalAndMarshaledMessage.Builder()
                .marshaled(marshalledMessage)
                .original(message)
                .build();
    }

    private MessageAdapter messageAdapter;

    protected abstract void sendMarshalledMessage(OriginalAndMarshaledMessage marshalledMessage);

    public void sendMessages(List<Message> value) {
        List<OriginalAndMarshaledMessage> marshalledMessages = value.stream()
                .map(this::marshal)
                .collect(toList());
        sendMarshalledMessages(marshalledMessages);
    }

    protected void sendMarshalledMessages(List<OriginalAndMarshaledMessage> marshalledMessage) {
        for(OriginalAndMarshaledMessage marshaledMessage : marshalledMessage) {
            sendMarshalledMessage(marshaledMessage);
        }
    }
}
