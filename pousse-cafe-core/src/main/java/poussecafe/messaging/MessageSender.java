package poussecafe.messaging;

import java.io.Closeable;
import java.util.List;
import poussecafe.exception.PousseCafeException;
import poussecafe.runtime.OriginalAndMarshaledMessage;

import static java.util.stream.Collectors.toList;

public abstract class MessageSender implements Closeable {

    protected MessageSender(MessageAdapter messageAdapter) {
        this.messageAdapter = messageAdapter;
    }

    public void sendMessage(Message message) {
        checkClosed();
        OriginalAndMarshaledMessage marshalledMessage = marshal(message);
        sendMarshalledMessage(marshalledMessage);
    }

    private void checkClosed() {
        if(closed) {
            throw new PousseCafeException("Message sender is closed");
        }
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
        checkClosed();
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

    @Override
    public void close() {
        if(!closed) {
            closed = true;
            actuallyClose();
        }
    }

    private boolean closed;

    protected void actuallyClose() {
        // May be overridden in children classes
    }
}
