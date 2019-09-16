package poussecafe.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageAdapter;
import poussecafe.messaging.MessageAdapterException;

public class JacksonMessageAdapter implements MessageAdapter {

    public JacksonMessageAdapter() {
        objectMapper = JacksonObjectMapperFactory.staticBuildMapper();
    }

    private ObjectMapper objectMapper;

    @Override
    public Message adaptSerializedMessage(Object serializedMessage) {
        try {
            return objectMapper.readValue((String) serializedMessage, Message.class);
        } catch (NoClassDefFoundError | Exception e) {
            throw buildSerializationException(serializedMessage, e);
        }
    }

    private MessageAdapterException buildSerializationException(Object serializedMessage, Throwable e) {
        return new MessageAdapterException("Unable to adapt serialized message " + serializedMessage, e);
    }

    @Override
    public String adaptMessage(Message message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new MessageAdapterException("Unable to adapt message " + message, e);
        }
    }

    JacksonMessageAdapter(ObjectMapper objectMapper) {
        Objects.requireNonNull(objectMapper);
        this.objectMapper = objectMapper;
    }
}
