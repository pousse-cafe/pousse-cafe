package poussecafe.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import java.io.IOException;
import poussecafe.domain.EntityAttributes;
import poussecafe.exception.PousseCafeException;
import poussecafe.jackson.JacksonObjectMapperBuilder;

public class PousseCafeTestObjectMapper {

    public PousseCafeTestObjectMapper() {
        objectMapper = new JacksonObjectMapperBuilder()
                .failOnUnknownProperties(true)
                .withDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE)
                .build();
    }

    public <K, D extends EntityAttributes<K>> void readJson(D dataImplementation,
            JsonNode dataJson) {
        try {
            objectMapper.readerForUpdating(dataImplementation).readValue(dataJson);
        } catch (IOException e) {
            throw new PousseCafeException("Unable to load data implementation", e);
        }
    }

    private ObjectMapper objectMapper;

    public ObjectMapper objectMapper() {
        return objectMapper;
    }
}
