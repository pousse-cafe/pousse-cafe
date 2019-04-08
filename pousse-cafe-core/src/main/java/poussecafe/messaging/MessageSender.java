package poussecafe.messaging;

import poussecafe.runtime.OriginalAndMarshaledMessage;

public abstract class MessageSender {

    protected MessageSender(MessageAdapter messageAdapter) {
        this.messageAdapter = messageAdapter;
    }

    public void sendMessage(Message message) {
        Object marshalledMessage = messageAdapter.adaptMessage(message);
        sendMarshalledMessage(new OriginalAndMarshaledMessage.Builder()
                .marshaled(marshalledMessage)
                .original(message)
                .build());
    }

    private MessageAdapter messageAdapter;

    protected abstract void sendMarshalledMessage(OriginalAndMarshaledMessage marshalledMessage);
}
