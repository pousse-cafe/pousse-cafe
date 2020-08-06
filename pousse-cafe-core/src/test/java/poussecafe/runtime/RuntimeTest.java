package poussecafe.runtime;

import java.util.Optional;
import java.util.Set;
import org.junit.Test;
import poussecafe.environment.AggregateDefinition;
import poussecafe.environment.AggregateServices;
import poussecafe.environment.Bundle;
import poussecafe.environment.BundleDefinition;
import poussecafe.environment.EntityImplementation;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenerDefinition;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.MessageImplementation;
import poussecafe.messaging.MessagingConnection;
import poussecafe.messaging.internal.InternalMessaging;
import poussecafe.storage.internal.InternalStorage;
import poussecafe.testmodule.SimpleAggregate;
import poussecafe.testmodule.SimpleAggregateData;
import poussecafe.testmodule.InternalSimpleAggregateDataAccess;
import poussecafe.testmodule.SimpleAggregateFactory;
import poussecafe.testmodule.SimpleAggregateRepository;
import poussecafe.testmodule.SimpleAggregateTouchRunner;
import poussecafe.testmodule.TestDomainEvent;
import poussecafe.testmodule.TestDomainEvent2;
import poussecafe.testmodule.TestDomainEvent3;
import poussecafe.testmodule.TestDomainEvent4;
import poussecafe.util.ReflectionUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class RuntimeTest {

    @Test
    public void entityServicesConfiguration() {
        givenBundle();
        whenCreatingRuntime();
        thenAggregateServicesAreConfigured();
    }

    private void givenBundle() {
        BundleDefinition bundleDefinition = new BundleDefinition.Builder()
                .withAggregateDefinition(new AggregateDefinition.Builder()
                        .withAggregateRoot(SimpleAggregate.class)
                        .withFactoryClass(SimpleAggregateFactory.class)
                        .withRepositoryClass(SimpleAggregateRepository.class)
                        .build())
                .withDomainProcess(DummyProcess.class)
                .withMessage(TestDomainEvent.class)
                .withMessage(TestDomainEvent2.class)
                .withMessage(TestDomainEvent3.class)
                .withMessage(TestDomainEvent4.class)
                .withMessageListener(new MessageListenerDefinition.Builder()
                        .containerClass(DummyProcess.class)
                        .method(ReflectionUtils.method(DummyProcess.class, "domainEventListenerWithDefaultId", TestDomainEvent.class))
                        .build())
                .withMessageListener(new MessageListenerDefinition.Builder()
                        .customId(Optional.of("customDomainEventListenerId"))
                        .containerClass(DummyProcess.class)
                        .method(ReflectionUtils.method(DummyProcess.class, "domainEventListenerWithCustomId", TestDomainEvent.class))
                        .build())
                .withMessageListener(new MessageListenerDefinition.Builder()
                        .containerClass(SimpleAggregateFactory.class)
                        .method(ReflectionUtils.method(SimpleAggregateFactory.class, "newSimpleAggregate", TestDomainEvent.class))
                        .build())
                .withMessageListener(new MessageListenerDefinition.Builder()
                        .containerClass(SimpleAggregateFactory.class)
                        .method(ReflectionUtils.method(SimpleAggregateFactory.class, "newSimpleAggregate", TestDomainEvent2.class))
                        .build())
                .withMessageListener(new MessageListenerDefinition.Builder()
                        .containerClass(SimpleAggregate.class)
                        .method(ReflectionUtils.method(SimpleAggregate.class, "touch", TestDomainEvent3.class))
                        .runner(Optional.of(SimpleAggregateTouchRunner.class))
                        .build())
                .withMessageListener(new MessageListenerDefinition.Builder()
                        .containerClass(SimpleAggregateRepository.class)
                        .method(ReflectionUtils.method(SimpleAggregateRepository.class, "delete", TestDomainEvent4.class))
                        .build())
                .build();

        bundle = new Bundle.Builder()
                .definition(bundleDefinition)
                .withEntityImplementation(new EntityImplementation.Builder()
                        .withEntityClass(SimpleAggregate.class)
                        .withDataFactory(SimpleAggregateData::new)
                        .withDataAccessFactory(InternalSimpleAggregateDataAccess::new)
                        .withStorage(InternalStorage.instance())
                        .build())
                .withMessageImplentation(new MessageImplementation.Builder()
                    .messageClass(TestDomainEvent.class)
                    .messageImplementationClass(TestDomainEvent.class)
                    .messaging(InternalMessaging.instance())
                    .build())
                .withMessageImplentation(new MessageImplementation.Builder()
                        .messageClass(TestDomainEvent2.class)
                        .messageImplementationClass(TestDomainEvent2.class)
                        .messaging(InternalMessaging.instance())
                        .build())
                .withMessageImplentation(new MessageImplementation.Builder()
                        .messageClass(TestDomainEvent3.class)
                        .messageImplementationClass(TestDomainEvent3.class)
                        .messaging(InternalMessaging.instance())
                        .build())
                .withMessageImplentation(new MessageImplementation.Builder()
                        .messageClass(TestDomainEvent4.class)
                        .messageImplementationClass(TestDomainEvent4.class)
                        .messaging(InternalMessaging.instance())
                        .build())
                .build();
    }

    private Bundle bundle;

    private void whenCreatingRuntime() {
        runtime = new Runtime.Builder().withBundle(bundle).build();
    }

    private Runtime runtime;

    private void thenAggregateServicesAreConfigured() {
        AggregateServices services;

        services = runtime.environment().aggregateServicesOf(SimpleAggregate.class).orElseThrow(PousseCafeException::new);
        assertThat(services.aggregateRootEntityClass(), equalTo(SimpleAggregate.class));
        assertThat(services.factory(), notNullValue());
        assertThat(services.repository(), notNullValue());
    }

    @Test
    public void messageListenersConfiguration() {
        givenBundle();
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
        givenBundle();
        whenCreatingRuntime();
        thenMessageReceiversIsStarted();
    }

    private void thenMessageReceiversIsStarted() {
        for(MessagingConnection connection : runtime.messagingConnections()) {
            assertTrue(connection.messageReceiver().isStarted());
        }
    }

    @Test
    public void threadPoolNotCreatedBeforeStart() {
        givenBundle();
        whenCreatingRuntime();
        thenHasThreadPool(false);
    }

    private void thenHasThreadPool(boolean hasThreadPool) {
        assertTrue(runtime.hasThreadPool() == hasThreadPool);
    }

    @Test
    public void threadPoolCreatedOnStart() {
        givenBundle();
        whenCreatingAndStartingRuntime();
        thenHasThreadPool(true);
    }

    private void whenCreatingAndStartingRuntime() {
        runtime = new Runtime.Builder().withBundle(bundle).buildAndStart();
    }
}
