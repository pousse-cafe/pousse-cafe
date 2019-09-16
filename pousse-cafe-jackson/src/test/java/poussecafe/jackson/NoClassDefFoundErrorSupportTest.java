package poussecafe.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageAdapterException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NoClassDefFoundErrorSupportTest {

    @Test(expected = MessageAdapterException.class)
    public void noClassDefFoundErrorHandled() throws Exception {
        givenJacksonMessageAdapterWithThrowingMapper();
        whenDeserializing();
    }

    private void givenJacksonMessageAdapterWithThrowingMapper() throws Exception {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(any(String.class), eq(Message.class))).thenThrow(NoClassDefFoundError.class);
        adapter = new JacksonMessageAdapter(objectMapper);
    }

    private JacksonMessageAdapter adapter;

    private void whenDeserializing() {
        adapter.adaptSerializedMessage("");
    }
}
