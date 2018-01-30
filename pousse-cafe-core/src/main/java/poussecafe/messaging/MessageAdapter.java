package poussecafe.messaging;

import java.util.HashMap;
import java.util.Map;
import poussecafe.exception.PousseCafeException;
import poussecafe.journal.SerializedMessage;

public class MessageAdapter {

    private Map<String, MessageWriter<Message>> writers = new HashMap<>();

    private Map<String, MessageReader<? extends Message>> readers = new HashMap<>();

    public Message adaptSerializedMessage(SerializedMessage serializedMessage) {
        MessageReader<? extends Message> reader = readers.get(serializedMessage.getType());
        if(reader != null) {
            return reader.read(serializedMessage.getData());
        } else {
            throw new PousseCafeException("No reader for type " + serializedMessage.getType());
        }
    }

    public SerializedMessage adaptMessage(Message message) {
        MessageWriter<Message> writer = writers.get(message.getType());
        if(writer != null) {
            return writer.write(message);
        } else {
            throw new PousseCafeException("No writer for type " + message.getType());
        }
    }
}
