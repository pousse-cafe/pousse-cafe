package poussecafe.source;

import java.util.Optional;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.Command;
import poussecafe.source.model.DomainEvent;
import poussecafe.source.model.Message;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.model.SourceModel;
import poussecafe.source.model.ProducedEvent;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class ModelAssertions {

    public void thenProcess1AggregatesFound() {
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
    }

    private Optional<Aggregate> aggregateRoot(String name) {
        return model.aggregate(name);
    }

    private SourceModel model;

    public void thenProcess1AggregateListenersFound() {
        Optional<MessageListener> listener0 = aggregateMessageListener("Aggregate1", "process1Listener0", "Command1");
        assertTrue(listener0.isPresent());
        assertTrue(listener0.orElseThrow().container().type() == MessageListenerContainerType.INNER_FACTORY);
        assertThat(listener0.orElseThrow().container().aggregateName().orElseThrow(), equalTo("Aggregate1"));
        assertThat(listener0.orElseThrow().consumedMessage(), equalTo(Message.command("Command1")));
        assertTrue(listener0.orElseThrow().processNames().contains("Process1"));

        Optional<MessageListener> listener1 = aggregateMessageListener("Aggregate1", "process1Listener1", "Event1");
        assertTrue(listener1.isPresent());
        assertTrue(listener1.orElseThrow().container().type() == MessageListenerContainerType.INNER_ROOT);
        assertThat(listener1.orElseThrow().container().aggregateName().orElseThrow(), equalTo("Aggregate1"));
        assertThat(listener1.orElseThrow().consumedMessage(), equalTo(Message.domainEvent("Event1")));
        assertTrue(listener1.orElseThrow().processNames().contains("Process1"));
        assertThat(listener1.orElseThrow().producedEvents(), hasItem(new ProducedEvent.Builder()
                .message(Message.domainEvent("Event2"))
                .required(true)
                .build()));
        assertThat(listener1.orElseThrow().consumesFromExternal(), equalTo(Optional.of("External1")));
        assertThat(listener1.orElseThrow().runnerName(), equalTo(Optional.of("Process1Listener1Runner")));

        Optional<MessageListener> listener2 = aggregateMessageListener("Aggregate2", "process1Listener2", "Event2");
        assertTrue(listener2.isPresent());
        assertThat(listener2.orElseThrow().container().aggregateName().orElseThrow(), equalTo("Aggregate2"));
        assertTrue(listener2.orElseThrow().container().type() == MessageListenerContainerType.STANDALONE_ROOT);
        assertThat(listener2.orElseThrow().consumedMessage().name(), equalTo("Event2"));
        assertTrue(listener2.orElseThrow().processNames().contains("Process1"));
        assertThat(listener2.orElseThrow().producedEvents(), hasItem(new ProducedEvent.Builder()
                .message(Message.domainEvent("Event3"))
                .required(false)
                .consumedByExternal(asList("External2"))
                .build()));
        assertThat(listener2.orElseThrow().runnerName(), equalTo(Optional.of("Process1Listener2Runner")));

        Optional<MessageListener> listener3 = aggregateMessageListener("Aggregate2", "process1Listener3", "Command2");
        assertTrue(listener3.isPresent());
        assertTrue(listener3.orElseThrow().container().type() == MessageListenerContainerType.STANDALONE_REPOSITORY);
        assertThat(listener3.orElseThrow().container().aggregateName().orElseThrow(), equalTo("Aggregate2"));
        assertThat(listener3.orElseThrow().consumedMessage(), equalTo(Message.command("Command2")));
        assertTrue(listener3.orElseThrow().processNames().contains("Process1"));
    }

    private Optional<MessageListener> aggregateMessageListener(String aggregateName, String listenerName, String messageName) {
        return model.aggregateListeners(aggregateName).stream()
                .filter(listener -> listener.methodName().equals(listenerName))
                .filter(listener -> listener.consumedMessage().name().equals(messageName))
                .findFirst();
    }

    public void thenProcessesFound() {
        assertTrue(model.process("Process1").isPresent());
        assertTrue(model.process("Process2").isPresent());
    }

    public void thenProcessesHaveListeners() {
        thenProcess1HasListeners();
        thenProcess2HasListeners();
    }

    public void thenProcess1HasListeners() {
        Optional<MessageListener> listener0 = processListener("Process1", "process1Listener0");
        assertTrue(listener0.isPresent());

        Optional<MessageListener> listener1 = processListener("Process1", "process1Listener1");
        assertTrue(listener1.isPresent());

        Optional<MessageListener> listener2 = processListener("Process1", "process1Listener2");
        assertTrue(listener2.isPresent());

        Optional<MessageListener> listener3 = processListener("Process1", "process1Listener3");
        assertTrue(listener3.isPresent());
    }

    public void thenProcess2HasListeners() {
        Optional<MessageListener> process2Listener0 = processListener("Process2", "process2Listener0");
        assertTrue(process2Listener0.isPresent());
    }

    private Optional<MessageListener> processListener(String process, String method) {
        return model.processListeners(process).stream()
                .filter(listener -> listener.methodName().equals(method))
                .findFirst();
    }

    public void thenHasEvent(String name) {
        assertTrue(model.events().stream().map(DomainEvent::simpleName).anyMatch(name::equals));
    }

    public void thenHasCommand(String name) {
        assertTrue(model.commands().stream().map(Command::simpleName).anyMatch(name::equals));
    }

    public ModelAssertions(SourceModel model) {
        requireNonNull(model);
        this.model = model;
    }
}
