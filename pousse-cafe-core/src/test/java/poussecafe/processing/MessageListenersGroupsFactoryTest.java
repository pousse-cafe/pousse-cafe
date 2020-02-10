package poussecafe.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.SimpleAggregate;
import poussecafe.domain.SimpleMessage;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.MessageConsumer;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenerType;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OriginalAndMarshaledMessage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class MessageListenersGroupsFactoryTest {

    @Test
    public void factoryProducesExpectedGroups() {
        givenMessage();
        givenMessageListeners();
        whenGroupingListeners();
        thenExpectedGroupsAreBuilt();
    }

    private void givenMessage() {
        message = new SimpleMessage();
    }

    private SimpleMessage message;

    private void givenMessageListeners() {
        MessageConsumer consumer = mock(MessageConsumer.class);

        listeners.add(new MessageListener.Builder()
                .id("custom")
                .shortId("custom")
                .consumer(consumer)
                .consumedMessageClass(SimpleMessage.class)
                .priority(MessageListenerType.CUSTOM)
                .build());

        listeners.add(new MessageListener.Builder()
                .id("process")
                .shortId("process")
                .consumer(consumer)
                .consumedMessageClass(SimpleMessage.class)
                .priority(MessageListenerType.DOMAIN_PROCESS)
                .build());

        listeners.add(new MessageListener.Builder()
                .id("factory")
                .shortId("factory")
                .consumer(consumer)
                .consumedMessageClass(SimpleMessage.class)
                .priority(MessageListenerType.FACTORY)
                .aggregateRootClass(Optional.of(SimpleAggregate.class))
                .build());

        @SuppressWarnings("rawtypes")
        AggregateMessageListenerRunner runner = mock(AggregateMessageListenerRunner.class);
        listeners.add(new MessageListener.Builder()
                .id("aggregate")
                .shortId("aggregate")
                .consumer(consumer)
                .consumedMessageClass(SimpleMessage.class)
                .priority(MessageListenerType.AGGREGATE)
                .aggregateRootClass(Optional.of(SimpleAggregate.class))
                .runner(Optional.of(runner))
                .build());

        listeners.add(new MessageListener.Builder()
                .id("repository")
                .shortId("repository")
                .consumer(consumer)
                .consumedMessageClass(SimpleMessage.class)
                .priority(MessageListenerType.REPOSITORY)
                .aggregateRootClass(Optional.of(SimpleAggregate.class))
                .build());
    }

    private List<MessageListener> listeners = new ArrayList<>();

    private void whenGroupingListeners() {
        ApplicationPerformanceMonitoring applicationPerformanceMonitoring = mock(ApplicationPerformanceMonitoring.class);
        MessageConsumptionHandler messageConsumptionHandler = mock(MessageConsumptionHandler.class);
        groups = new MessageListenersGroupsFactory.Builder()
                .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                .consumptionId("consumtionId")
                .failFast(false)
                .message(new OriginalAndMarshaledMessage.Builder()
                        .marshaled(message)
                        .original(message)
                        .build())
                .messageConsumptionHandler(messageConsumptionHandler)
                .build()
                .buildMessageListenerGroups(listeners);
    }

    private List<MessageListenerGroup> groups;

    private void thenExpectedGroupsAreBuilt() {
        assertThat(groups.get(0).aggregateRootClass().orElseThrow(), is(SimpleAggregate.class));
        assertThat(groups.get(0).listeners().size(), is(3));
        assertThat(groups.get(0).listeners().get(0).id(), is("repository"));
        assertThat(groups.get(0).listeners().get(1).id(), is("aggregate"));
        assertThat(groups.get(0).listeners().get(2).id(), is("factory"));

        assertThat(groups.get(1).listeners().size(), is(1));
        assertThat(groups.get(1).listeners().get(0).id(), is("process"));

        assertThat(groups.get(2).listeners().size(), is(1));
        assertThat(groups.get(2).listeners().get(0).id(), is("custom"));
    }
}
