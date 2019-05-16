package poussecafe.messaging.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageAdapter;
import poussecafe.messaging.MessageAdapterException;

public class SerializingMessageAdapter implements MessageAdapter {

    @Override
    public Message adaptSerializedMessage(Object serializedMessage) {
        try {
            byte[] bytes = (byte[]) serializedMessage;
            ByteArrayInputStream buffer = new ByteArrayInputStream(bytes);
            ObjectInputStream stream = new ObjectInputStream(buffer);
            Message message = (Message) stream.readObject();
            stream.close();
            return message;
        } catch (ClassNotFoundException | IOException e) {
            throw new MessageAdapterException("Unable to deserialize message " + serializedMessage, e);
        }
    }

    @Override
    public Object adaptMessage(Message message) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(buffer);
            outputStream.writeObject(message); // NOSONAR
            outputStream.close();
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new MessageAdapterException("Unable to serialize message " + message, e);
        }
    }
}
