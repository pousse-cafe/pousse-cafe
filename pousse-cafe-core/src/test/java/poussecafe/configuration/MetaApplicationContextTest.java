package poussecafe.configuration;

import java.util.Set;
import org.junit.Test;
import poussecafe.consequence.CommandProcessor;
import poussecafe.consequence.ConsequenceListener;
import poussecafe.consequence.ConsequenceListenerRoutingKey;
import poussecafe.consequence.ConsequenceReceiver;
import poussecafe.consequence.Source;
import poussecafe.domain.SimpleAggregate;
import poussecafe.process.SimpleProcessManager;
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
        configuration.registerProcessManagerConfiguration(new SimpleProcessManagerConfiguration());
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

        services = context.getStorableServices(SimpleProcessManager.class);
        assertThat(services.getStorableClass(), equalTo(SimpleProcessManager.class));
        assertThat(services.getFactory(), notNullValue());
        assertThat(services.getRepository(), notNullValue());
    }

    @Test
    public void consequenceListenersConfiguration() {
        givenApplicationConfiguration();
        whenCreatingContext();
        thenConsequenceListenersAreConfigured();
    }

    private void thenConsequenceListenersAreConfigured() {
        Set<ConsequenceListener> listeners;

        listeners = context.getConsequenceListeners(
                new ConsequenceListenerRoutingKey(Source.DEFAULT_DOMAIN_EVENT_SOURCE, TestDomainEvent.class));
        assertThat(listeners.size(), is(2));

        listeners = context.getConsequenceListeners(
                new ConsequenceListenerRoutingKey(Source.DEFAULT_COMMAND_SOURCE, TestCommand.class));
        assertThat(listeners.size(), is(1));

        listeners = context.getConsequenceListeners(
                new ConsequenceListenerRoutingKey(Source.DEFAULT_COMMAND_SOURCE, AnotherTestCommand.class));
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
    public void consequenceReceiversStarted() {
        givenApplicationConfiguration();
        whenCreatingContext();
        thenConsequenceReceiversAreStarted();
    }

    private void thenConsequenceReceiversAreStarted() {
        for (ConsequenceReceiver receiver : configuration.getConsequenceReceivers()) {
            assertTrue(receiver.isStarted());
        }
    }
}
