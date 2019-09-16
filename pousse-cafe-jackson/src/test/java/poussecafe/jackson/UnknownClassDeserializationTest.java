package poussecafe.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import org.junit.Test;
import poussecafe.messaging.MessageAdapterException;

public class UnknownClassDeserializationTest {

    @Test(expected = MessageAdapterException.class)
    public void deserializationWithInternalUnknownClassFails() throws IOException {
        givenJsonWithUnknownInternalClass();
        whenDeserializing();
    }

    private void givenJsonWithUnknownInternalClass() {
        ObjectNode node = newObjectNodeWithClass("poussecafe.jackson.SimpleMessage");
        node.put("payload", "data");
        node.set("polymorphicPayload", newObjectNodeWithClass("UnknownClass"));
        givenJson(node);
    }

    private void givenJson(ObjectNode node) {
        try {
            json = JacksonObjectMapperFactory.staticBuildMapper().writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to convert node", e);
        }
    }

    private ObjectNode newObjectNodeWithClass(String className) {
        ObjectNode node = newObjectNode();
        node.put("@class", className);
        return node;
    }

    private ObjectNode newObjectNode() {
        return new ObjectNode(JsonNodeFactory.instance);
    }

    private String json;

    private void whenDeserializing() throws IOException {
        adapter.adaptSerializedMessage(json);
    }

    private JacksonMessageAdapter adapter = new JacksonMessageAdapter();

    @Test(expected = MessageAdapterException.class)
    public void deserializationWithUnknownClassFails() throws IOException {
        givenJsonWithUnknownClass();
        whenDeserializing();
    }

    private void givenJsonWithUnknownClass() {
        givenJson(newObjectNodeWithClass("UnknownClass"));
    }
}
