package poussecafe.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageAdapter;

public class JacksonMessageAdapter implements MessageAdapter {

    @Override
    public Message adaptSerializedMessage(Object serializedMessage) {
        try {
            return objectMapper.readValue((String) serializedMessage, Message.class);
        } catch (Exception e) {
            throw new PousseCafeException("Unable to adapt serialized message " + serializedMessage, e);
        }
    }

    private ObjectMapper objectMapper = initObjectMapper();

    protected ObjectMapper initObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setConfig(mapper.getSerializationConfig().without(SerializationFeature.FAIL_ON_EMPTY_BEANS));
        mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        return mapper;
    }

    @Override
    public String adaptMessage(Message message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new PousseCafeException("Unable to adapt message " + message, e);
        }
    }

}
