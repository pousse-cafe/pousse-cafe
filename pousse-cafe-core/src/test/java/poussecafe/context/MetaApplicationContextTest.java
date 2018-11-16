package poussecafe.context;

import java.util.Set;
import org.junit.Test;
import poussecafe.domain.EntityDefinition;
import poussecafe.domain.EntityImplementation;
import poussecafe.domain.SimpleAggregate;
import poussecafe.domain.SimpleAggregateData;
import poussecafe.domain.SimpleAggregateDataAccess;
import poussecafe.domain.SimpleAggregateFactory;
import poussecafe.domain.SimpleAggregateRepository;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.Messaging;
import poussecafe.storage.memory.InMemoryStorage;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MetaApplicationContextTest {

    private MetaApplicationContext context = new MetaApplicationContext();

    @Test
    public void entityServicesConfiguration() {
        givenApplicationConfiguration();
        whenCreatingContext();
        thenEntityServicesAreConfigured();
    }

    private void givenApplicationConfiguration() {
        context.environment().defineEntity(new EntityDefinition.Builder()
                .withEntityClass(SimpleAggregate.class)
                .withFactoryClass(SimpleAggregateFactory.class)
                .withRepositoryClass(SimpleAggregateRepository.class)
                .build());

        context.environment().implementEntity(new EntityImplementation.Builder()
                .withEntityClass(SimpleAggregate.class)
                .withDataFactory(SimpleAggregateData::new)
                .withDataAccessFactory(SimpleAggregateDataAccess::new)
                .withStorage(InMemoryStorage.instance())
                .build());

        context.environment().defineProcess(DummyProcess.class);
    }

    private void whenCreatingContext() {
        context.start();
    }

    private void thenEntityServicesAreConfigured() {
        EntityServices services;

        services = context.getEntityServices(SimpleAggregate.class);
        assertThat(services.getEntityClass(), equalTo(SimpleAggregate.class));
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

        listeners = context.getMessageListeners(TestDomainEvent.class);
        assertThat(listeners.size(), is(2));
    }

    @Test
    public void messageReceiversStarted() {
        givenApplicationConfiguration();
        whenCreatingContext();
        thenMessageReceiversIsStarted();
    }

    private void thenMessageReceiversIsStarted() {
        for(Messaging messaging : context.environment().getMessagings()) {
            assertTrue(messaging.messageReceiver().isStarted());
        }
    }
}
