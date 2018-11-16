package poussecafe.messaging;

public interface MessageAdapter {

    Message adaptSerializedMessage(Object serializedMessage);

    Object adaptMessage(Message message);
}
