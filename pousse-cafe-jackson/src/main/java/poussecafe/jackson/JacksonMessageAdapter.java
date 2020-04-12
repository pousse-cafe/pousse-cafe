package poussecafe.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import java.util.Objects;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageAdapter;
import poussecafe.messaging.MessageAdapterException;
import poussecafe.messaging.UnknownMessageTypeException;

public class JacksonMessageAdapter implements MessageAdapter {

    public JacksonMessageAdapter() {
        objectMapper = JacksonObjectMapperFactory.staticBuildMapper();
    }

    private ObjectMapper objectMapper;

    @Override
    public Message adaptSerializedMessage(Object serializedMessage) {
        try {
            return objectMapper.readValue((String) serializedMessage, Message.class);
        } catch (InvalidTypeIdException e) {
            if(e.getBaseType().getRawClass() == Message.class) {
                throw new UnknownMessageTypeException(e);
            } else {
                throw buildDeserializationException(serializedMessage, e);
            }
        } catch (NoClassDefFoundError | Exception e) {
            throw buildDeserializationException(serializedMessage, e);
        }
    }

    private MessageAdapterException buildDeserializationException(Object serializedMessage, Throwable e) {
        return new MessageAdapterException("Unable to deserialize message " + serializedMessage, e);
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
