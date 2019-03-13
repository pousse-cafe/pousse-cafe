package poussecafe.context;

import java.util.Optional;
import java.util.Set;
import org.junit.Test;
import poussecafe.domain.SimpleAggregate;
import poussecafe.domain.SimpleAggregateData;
import poussecafe.domain.SimpleAggregateDataAccess;
import poussecafe.domain.SimpleAggregateFactory;
import poussecafe.domain.SimpleAggregateRepository;
import poussecafe.domain.SimpleAggregateTouchRunner;
import poussecafe.environment.AggregateDefinition;
import poussecafe.environment.AggregateServices;
import poussecafe.environment.BoundedContext;
import poussecafe.environment.BoundedContextDefinition;
import poussecafe.environment.EntityImplementation;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.MessageImplementation;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerDefinition;
import poussecafe.messaging.MessagingConnection;
import poussecafe.messaging.internal.InternalMessaging;
import poussecafe.runtime.Runtime;
import poussecafe.storage.internal.InternalStorage;
import poussecafe.util.ReflectionUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RuntimeTest {

    @Test
    public void entityServicesConfiguration() {
        givenBoundedContext();
        whenCreatingRuntime();
        thenAggregateServicesAreConfigured();
    }

    private void givenBoundedContext() {
        BoundedContextDefinition boundedContextDefinition = new BoundedContextDefinition.Builder()
                .withAggregateDefinition(new AggregateDefinition.Builder()
                        .withEntityClass(SimpleAggregate.class)
                        .withFactoryClass(SimpleAggregateFactory.class)
                        .withRepositoryClass(SimpleAggregateRepository.class)
                        .build())
                .withDomainProcess(DummyProcess.class)
                .withMessage(TestDomainEvent.class)
                .withMessage(TestDomainEvent2.class)
                .withMessage(TestDomainEvent3.class)
                .withMessage(TestDomainEvent4.class)
                .withMessageListener(new MessageListenerDefinition.Builder()
                        .method(ReflectionUtils.method(DummyProcess.class, "domainEventListenerWithDefaultId", TestDomainEvent.class))
                        .build())
                .withMessageListener(new MessageListenerDefinition.Builder()
                        .customId(Optional.of("customDomainEventListenerId"))
                        .method(ReflectionUtils.method(DummyProcess.class, "domainEventListenerWithCustomId", TestDomainEvent.class))
                        .build())
                .withMessageListener(new MessageListenerDefinition.Builder()
                        .method(ReflectionUtils.method(SimpleAggregateFactory.class, "newSimpleAggregate", TestDomainEvent.class))
                        .build())
                .withMessageListener(new MessageListenerDefinition.Builder()
                        .method(ReflectionUtils.method(SimpleAggregateFactory.class, "newSimpleAggregate", TestDomainEvent2.class))
                        .build())
                .withMessageListener(new MessageListenerDefinition.Builder()
                        .method(ReflectionUtils.method(SimpleAggregate.class, "touch", TestDomainEvent3.class))
                        .runner(Optional.of(SimpleAggregateTouchRunner.class))
                        .build())
                .withMessageListener(new MessageListenerDefinition.Builder()
                        .method(ReflectionUtils.method(SimpleAggregateRepository.class, "delete", TestDomainEvent4.class))
                        .build())
                .build();

        boundedContext = new BoundedContext.Builder()
                .definition(boundedContextDefinition)
                .withEntityImplementation(new EntityImplementation.Builder()
                        .withEntityClass(SimpleAggregate.class)
                        .withDataFactory(SimpleAggregateData::new)
                        .withDataAccessFactory(SimpleAggregateDataAccess::new)
                        .withStorage(InternalStorage.instance())
                        .build())
                .withMessageImplentation(new MessageImplementation.Builder()
                    .withMessageClass(TestDomainEvent.class)
                    .withMessageImplementationClass(TestDomainEvent.class)
                    .withMessaging(InternalMessaging.instance())
                    .build())
                .withMessageImplentation(new MessageImplementation.Builder()
                        .withMessageClass(TestDomainEvent2.class)
                        .withMessageImplementationClass(TestDomainEvent2.class)
                        .withMessaging(InternalMessaging.instance())
                        .build())
                .withMessageImplentation(new MessageImplementation.Builder()
                        .withMessageClass(TestDomainEvent3.class)
                        .withMessageImplementationClass(TestDomainEvent3.class)
                        .withMessaging(InternalMessaging.instance())
                        .build())
                .withMessageImplentation(new MessageImplementation.Builder()
                        .withMessageClass(TestDomainEvent4.class)
                        .withMessageImplementationClass(TestDomainEvent4.class)
                        .withMessaging(InternalMessaging.instance())
                        .build())
                .build();
    }

    private BoundedContext boundedContext;

    private void whenCreatingRuntime() {
        runtime = new Runtime.Builder().withBoundedContext(boundedContext).build();
    }

    private Runtime runtime;

    private void thenAggregateServicesAreConfigured() {
        AggregateServices services;

        services = runtime.environment().aggregateServicesOf(SimpleAggregate.class).orElseThrow(PousseCafeException::new);
        assertThat(services.getEntityClass(), equalTo(SimpleAggregate.class));
        assertThat(services.getFactory(), notNullValue());
        assertThat(services.getRepository(), notNullValue());
    }

    @Test
    public void messageListenersConfiguration() {
        givenBoundedContext();
        whenCreatingRuntime();
        thenMessageListenersAreConfigured();
    }

    private void thenMessageListenersAreConfigured() {
        Set<MessageListener> listeners;

        listeners = runtime.environment().messageListenersOf(TestDomainEvent.class);
        assertThat(listeners.size(), is(3));
    }

    @Test
    public void messageReceiversStarted() {
        givenBoundedContext();
        whenCreatingRuntime();
        thenMessageReceiversIsStarted();
    }

    private void thenMessageReceiversIsStarted() {
        for(MessagingConnection connection : runtime.messagingConnections()) {
            assertTrue(connection.messageReceiver().isStarted());
        }
    }
}
