package poussecafe.source;

import java.io.IOException;
import java.util.Optional;
import org.junit.Test;
import poussecafe.source.model.Message;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.model.ProducedEvent;

import static java.util.Arrays.asList;
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
        Optional<MessageListener> listener0 = aggregateMessageListener("process1Listener0");
        assertTrue(listener0.isPresent());
        assertTrue(listener0.orElseThrow().container().type() == MessageListenerContainerType.FACTORY);
        assertThat(listener0.orElseThrow().container().aggregateName().orElseThrow(), equalTo("Aggregate1"));
        assertThat(listener0.orElseThrow().consumedMessage(), equalTo(Message.command("Command1")));
        assertTrue(listener0.orElseThrow().processNames().contains("Process1"));

        Optional<MessageListener> listener1 = aggregateMessageListener("process1Listener1");
        assertTrue(listener1.isPresent());
        assertTrue(listener1.orElseThrow().container().type() == MessageListenerContainerType.ROOT);
        assertThat(listener1.orElseThrow().container().aggregateName().orElseThrow(), equalTo("Aggregate1"));
        assertThat(listener1.orElseThrow().consumedMessage(), equalTo(Message.domainEvent("Event1")));
        assertTrue(listener1.orElseThrow().processNames().contains("Process1"));
        assertThat(listener1.orElseThrow().producedEvents(), hasItem(new ProducedEvent.Builder()
                .message(Message.domainEvent("Event2"))
                .required(true)
                .build()));
        assertThat(listener1.orElseThrow().consumesFromExternal(), equalTo(Optional.of("External1")));

        Optional<MessageListener> listener2 = aggregateMessageListener("process1Listener2");
        assertTrue(listener2.isPresent());
        assertThat(listener2.orElseThrow().container().aggregateName().orElseThrow(), equalTo("Aggregate2"));
        assertTrue(listener2.orElseThrow().container().type() == MessageListenerContainerType.ROOT);
        assertThat(listener2.orElseThrow().consumedMessage().name(), equalTo("Event2"));
        assertTrue(listener2.orElseThrow().processNames().contains("Process1"));
        assertThat(listener2.orElseThrow().producedEvents(), hasItem(new ProducedEvent.Builder()
                .message(Message.domainEvent("Event3"))
                .required(false)
                .consumedByExternal(asList("External2"))
                .build()));

        Optional<MessageListener> listener3 = aggregateMessageListener("process1Listener3");
        assertTrue(listener3.isPresent());
        assertTrue(listener3.orElseThrow().container().type() == MessageListenerContainerType.REPOSITORY);
        assertThat(listener3.orElseThrow().container().aggregateName().orElseThrow(), equalTo("Aggregate2"));
        assertThat(listener3.orElseThrow().consumedMessage(), equalTo(Message.command("Command2")));
        assertTrue(listener3.orElseThrow().processNames().contains("Process1"));
    }

    private Optional<MessageListener> aggregateMessageListener(String listenerName) {
        return model().messageListeners().stream()
                .filter(listener -> listener.methodName().equals(listenerName))
                .findFirst();
    }
}
