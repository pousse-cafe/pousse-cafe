package poussecafe.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import poussecafe.domain.EntityData;
import poussecafe.exception.PousseCafeException;

public class JsonDataReader {

    public JsonDataReader() {
        objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTypingAsProperty(DefaultTyping.JAVA_LANG_OBJECT, "@class");
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public <K, D extends EntityData<K>> void readJson(D dataImplementation,
            JsonNode dataJson) {
        try {
            objectMapper.readerForUpdating(dataImplementation).readValue(dataJson);
        } catch (IOException e) {
            throw new PousseCafeException("Unable to load data implementation", e);
        }
    }

    private ObjectMapper objectMapper;
}
