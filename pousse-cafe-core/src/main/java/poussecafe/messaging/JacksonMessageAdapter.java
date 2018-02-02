package poussecafe.messaging;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import poussecafe.exception.PousseCafeException;
import poussecafe.journal.SerializedMessage;

public class JacksonMessageAdapter implements MessageAdapter {

    @Override
    public Message adaptSerializedMessage(SerializedMessage serializedMessage) {
        String type = serializedMessage.getType();
        try {
            @SuppressWarnings("unchecked")
            Class<? extends Message> messageClass = (Class<? extends Message>) Class.forName(type);
            return objectMapper.readValue(serializedMessage.getData(), messageClass);
        } catch (Exception e) {
            throw new PousseCafeException("Unable to adapt serialized message " + serializedMessage, e);
        }
    }

    private ObjectMapper objectMapper = initObjectMapper();

    protected ObjectMapper initObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        return mapper;
    }

    @Override
    public SerializedMessage adaptMessage(Message message) {
        try {
            String messageId = message.getId();
            String type = message.getClass().getName();
            String data = objectMapper.writeValueAsString(message);
            return new SerializedMessage.Builder()
                    .withId(messageId)
                    .withType(type)
                    .withData(data)
                    .build();
        } catch (JsonProcessingException e) {
            throw new PousseCafeException("Unable to adapt message " + message, e);
        }
    }

}
