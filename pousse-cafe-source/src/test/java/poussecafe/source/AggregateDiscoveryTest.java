package poussecafe.source;

import java.io.IOException;
import java.util.Optional;
import org.junit.Test;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.Message;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.ProducedEvent;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
        Optional<Aggregate> aggregate1 = aggregateRoot("Aggregate1");
        assertTrue(aggregate1.isPresent());
        assertThat(aggregate1.orElseThrow().onAddProducedEvents().size(), is(1));
        assertThat(aggregate1.orElseThrow().onAddProducedEvents(), hasItem(new ProducedEvent.Builder()
                .message(Message.domainEvent("Event5"))
                .required(true)
                .build()));

        Optional<Aggregate> aggregate2 = aggregateRoot("Aggregate2");
        assertTrue(aggregate2.isPresent());

        assertThat(aggregate2.orElseThrow().onDeleteProducedEvents().size(), is(1));
        assertThat(aggregate2.orElseThrow().onDeleteProducedEvents(), hasItem(new ProducedEvent.Builder()
                .message(Message.domainEvent("Event6"))
                .required(true)
                .build()));

        assertThat(aggregate2.orElseThrow().onUpdateProducedEvents().size(), is(1));
        assertThat(aggregate2.orElseThrow().onUpdateProducedEvents(), hasItem(new ProducedEvent.Builder()
                .message(Message.domainEvent("Event7"))
                .required(true)
                .build()));
    }

    private Optional<Aggregate> aggregateRoot(String name) {
        return model().aggregateRoot(name);
    }

    private void thenAggregateListenersFound() {
        Optional<MessageListener> listener0 = aggregateMessageListener("Aggregate1", "process1Listener0", "Command1");
        assertTrue(listener0.isPresent());

        Optional<MessageListener> listener1 = aggregateMessageListener("Aggregate1", "process1Listener1", "Event1");
        assertTrue(listener1.isPresent());

        Optional<MessageListener> listener2 = aggregateMessageListener("Aggregate2", "process1Listener2", "Event2");
        assertTrue(listener2.isPresent());

        Optional<MessageListener> listener3 = aggregateMessageListener("Aggregate2", "process1Listener3", "Command2");
        assertTrue(listener3.isPresent());
    }

    private Optional<MessageListener> aggregateMessageListener(String aggregateName, String listenerName, String messageName) {
        return model().aggregateListeners(aggregateName).stream()
                .filter(listener -> listener.methodName().equals(listenerName))
                .filter(listener -> listener.consumedMessage().name().equals(messageName))
                .findFirst();
    }
}
