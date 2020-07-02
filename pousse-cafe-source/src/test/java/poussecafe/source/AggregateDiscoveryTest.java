package poussecafe.source;

import java.io.IOException;
import java.util.Optional;
import org.junit.Test;
import poussecafe.source.model.AggregateRootSource;
import poussecafe.source.model.MessageListenerSource;

import static org.junit.Assert.assertTrue;

public class AggregateDiscoveryTest extends DiscoveryTest {

    @Test
    public void findAggregates() throws IOException {
        givenScanner();
        whenIncludingTestModelTree();
        thenAggregateRootsFound();
        thenAggregateListenersFound();
    }

    private void thenAggregateRootsFound() {
        assertTrue(aggregateRoot("Aggregate1").isPresent());
        assertTrue(aggregateRoot("Aggregate2").isPresent());
    }

    private Optional<AggregateRootSource> aggregateRoot(String name) {
        return model().aggregateRoot(name);
    }

    private void thenAggregateListenersFound() {
        Optional<MessageListenerSource> listener1 = aggregateMessageListener("Aggregate1", "process1Listener1", "Event1");
        assertTrue(listener1.isPresent());
        assertTrue(listener1.orElseThrow().processNames().contains("Process1"));

        Optional<MessageListenerSource> listener2 = aggregateMessageListener("Aggregate2", "process1Listener2", "Event2");
        assertTrue(listener2.isPresent());
        assertTrue(listener2.orElseThrow().processNames().contains("Process1"));
    }

    private Optional<MessageListenerSource> aggregateMessageListener(String aggregateName, String listenerName, String messageName) {
        return model().aggregateRootListeners(aggregateName).stream()
                .filter(listener -> listener.methodName().equals(listenerName))
                .filter(listener -> listener.messageName().equals(messageName))
                .findFirst();
    }
}
