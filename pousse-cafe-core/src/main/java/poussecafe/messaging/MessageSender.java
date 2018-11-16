package poussecafe.messaging;

public abstract class MessageSender {

    protected MessageSender(MessageAdapter messageAdapter) {
        this.messageAdapter = messageAdapter;
    }

    public void sendMessage(Message message) {
        Object marshalledMessage = messageAdapter.adaptMessage(message);
        sendMarshalledMessage(marshalledMessage);
    }

    private MessageAdapter messageAdapter;

    protected abstract void sendMarshalledMessage(Object marshalledMessage);
}
