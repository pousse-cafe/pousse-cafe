package poussecafe.messaging;

public class TransparentMessageAdapter implements MessageAdapter {

    @Override
    public Object adaptMessage(Message message) {
        return message;
    }

    @Override
    public Message adaptSerializedMessage(Object serializedMessage) {
        return (Message) serializedMessage;
    }
}
