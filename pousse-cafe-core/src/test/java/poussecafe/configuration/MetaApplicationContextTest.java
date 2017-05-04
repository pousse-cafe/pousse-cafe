package poussecafe.configuration;

import java.util.Set;
import org.junit.Test;
import poussecafe.domain.SimpleAggregate;
import poussecafe.messaging.CommandProcessor;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerRoutingKey;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.Queue;
import poussecafe.util.FieldAccessor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MetaApplicationContextTest {

    private MetaApplicationConfiguration configuration;

    private MetaApplicationContext context;

    @Test
    public void storableServicesConfiguration() {
        givenApplicationConfiguration();
        whenCreatingContext();
        thenStorableServicesAreConfigured();
    }

    private void givenApplicationConfiguration() {
        configuration = new MetaApplicationConfiguration();
        configuration.registerAggregate(new SimpleAggregateConfiguration());
        configuration.registerWorkflow(new DummyWorkflow());
    }

    private void whenCreatingContext() {
        context = new MetaApplicationContext(configuration);
    }

    private void thenStorableServicesAreConfigured() {
        StorableServices services;

        services = context.getStorableServices(SimpleAggregate.class);
        assertThat(services.getStorableClass(), equalTo(SimpleAggregate.class));
        assertThat(services.getFactory(), notNullValue());
        assertThat(services.getRepository(), notNullValue());
    }

    @Test
    public void messageListenersConfiguration() {
        givenApplicationConfiguration();
        whenCreatingContext();
        thenMessageListenersAreConfigured();
    }

    private void thenMessageListenersAreConfigured() {
        Set<MessageListener> listeners;

        listeners = context.getMessageListeners(
                new MessageListenerRoutingKey(Queue.DEFAULT_DOMAIN_EVENT_QUEUE, TestDomainEvent.class));
        assertThat(listeners.size(), is(2));

        listeners = context.getMessageListeners(
                new MessageListenerRoutingKey(Queue.DEFAULT_COMMAND_QUEUE, TestCommand.class));
        assertThat(listeners.size(), is(1));

        listeners = context.getMessageListeners(
                new MessageListenerRoutingKey(Queue.DEFAULT_COMMAND_QUEUE, AnotherTestCommand.class));
        assertThat(listeners.size(), is(1));
    }

    @Test
    public void commandProcessorConfiguration() {
        givenApplicationConfiguration();
        whenCreatingContext();
        thenCommandProcessorIsConfigured();
    }

    private void thenCommandProcessorIsConfigured() {
        CommandProcessor processor = context.getCommandProcessor();
        assertThat(FieldAccessor.getFieldValue(processor, "router"), notNullValue());
    }

    @Test
    public void messageReceiversStarted() {
        givenApplicationConfiguration();
        whenCreatingContext();
        thenMessageReceiversAreStarted();
    }

    private void thenMessageReceiversAreStarted() {
        for (MessageReceiver receiver : configuration.getMessageReceivers()) {
            assertTrue(receiver.isStarted());
        }
    }
}
