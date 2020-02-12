package poussecafe.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import poussecafe.domain.EntityAttributes;
import poussecafe.exception.PousseCafeException;
import poussecafe.jackson.JacksonObjectMapperFactory;

public class PousseCafeTestObjectMapper {

    public PousseCafeTestObjectMapper() {
        objectMapper = JacksonObjectMapperFactory.staticBuildMapper();
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
