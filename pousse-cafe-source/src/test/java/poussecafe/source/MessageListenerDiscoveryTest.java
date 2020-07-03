package poussecafe.source;

import java.io.IOException;
import java.util.Optional;
import org.junit.Test;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.model.MessageListenerSource;
import poussecafe.source.model.ProducedEvent;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class MessageListenerDiscoveryTest extends DiscoveryTest {

    @Test
    public void findMessageListeners() throws IOException {
        givenScanner();
        whenIncludingTestModelTree();
        thenAggregateListenersFound();
    }

    private void thenAggregateListenersFound() {
        Optional<MessageListenerSource> listener1 = aggregateMessageListener("process1Listener1");
        assertTrue(listener1.isPresent());
        assertTrue(listener1.orElseThrow().container().type() == MessageListenerContainerType.ROOT);
        assertThat(listener1.orElseThrow().container().aggregateName().orElseThrow(), equalTo("Aggregate1"));
        assertThat(listener1.orElseThrow().messageName(), equalTo("Event1"));
        assertTrue(listener1.orElseThrow().processNames().contains("Process1"));
        assertThat(listener1.orElseThrow().producedEvents(), hasItem(new ProducedEvent.Builder()
                .eventName("Event2")
                .required(true)
                .build()));

        Optional<MessageListenerSource> listener2 = aggregateMessageListener("process1Listener2");
        assertTrue(listener2.isPresent());
        assertThat(listener2.orElseThrow().container().aggregateName().orElseThrow(), equalTo("Aggregate2"));
        assertTrue(listener2.orElseThrow().container().type() == MessageListenerContainerType.ROOT);
        assertThat(listener2.orElseThrow().messageName(), equalTo("Event2"));
        assertTrue(listener2.orElseThrow().processNames().contains("Process1"));
        assertThat(listener2.orElseThrow().producedEvents(), hasItem(new ProducedEvent.Builder()
                .eventName("Event3")
                .required(false)
                .build()));
    }

    private Optional<MessageListenerSource> aggregateMessageListener(String listenerName) {
        return model().messageListeners().stream()
                .filter(listener -> listener.methodName().equals(listenerName))
                .findFirst();
    }
}
