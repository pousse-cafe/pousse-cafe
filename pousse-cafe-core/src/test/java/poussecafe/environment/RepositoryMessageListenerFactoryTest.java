package poussecafe.environment;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import poussecafe.environment.RepositoryListenerTestComponents.Event;
import poussecafe.environment.RepositoryListenerTestComponents.Repository;
import poussecafe.environment.RepositoryListenerTestComponents.Root;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RepositoryMessageListenerFactoryTest {

    @Before
    public void beforeEach() {
        givenRepositoryMessageListenerFactory();
    }

    private void givenRepositoryMessageListenerFactory() {
        var apmMocker = new ApplicationPerformanceMonitoringMocker();
        var environmentMocker = new EnvironmentMocker.Builder()
                .aggregateRootClass(Root.class)
                .repositoryClass(Repository.class)
                .build();
        var transactionRunnerLocator = TransactionRunnerLocatorMock.mock(Root.class);

        factory = new RepositoryMessageListenerFactory.Builder()
                .applicationPerformanceMonitoring(apmMocker.mock())
                .environment(environmentMocker.mock())
                .transactionRunnerLocator(transactionRunnerLocator)
                .build();
    }

    private RepositoryMessageListenerFactory factory;

    @Test
    public void legacyListenerGetsLegacyMessageConsumer() {
        givenLegacyRepositoryListener();
        whenBuildingMessageListener();
        thenMessageConsumerIsLegacy();
    }

    private void givenLegacyRepositoryListener() {
        givenRepositoryListener("legacyDelete");
    }

    private void givenRepositoryListener(String methodName) {
        try {
            definition = new MessageListenerDefinition.Builder()
                    .collisionSpace(Optional.of("Root"))
                    .containerClass(Repository.class)
                    .method(Repository.class.getDeclaredMethod(methodName, Event.class))
                    .build();
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }

    private MessageListenerDefinition definition;

    private void whenBuildingMessageListener() {
        listener = factory.buildMessageListener(definition);
    }

    private MessageListener listener;

    private void thenMessageConsumerIsLegacy() {
        var messageConsumer = listener.consumer();
        assertFalse(messageConsumer instanceof DeleteOneOrNoneAggregateMessageConsumer);
        assertFalse(messageConsumer instanceof DeleteSeveralAggregatesMessageConsumer);
    }

    @Test
    public void singleIdentifierListenerGetsDeleteOneOrNoneAggregateMessageConsumer() {
        givenSingleIdentifierRepositoryListener();
        whenBuildingMessageListener();
        thenMessageConsumerIsDeleteOneOrNone();
    }

    private void givenSingleIdentifierRepositoryListener() {
        givenRepositoryListener("deleteSingle");
    }

    private void thenMessageConsumerIsDeleteOneOrNone() {
        var messageConsumer = listener.consumer();
        assertTrue(messageConsumer instanceof DeleteOneOrNoneAggregateMessageConsumer);
    }

    @Test
    public void optionalIdentifierListenerGetsDeleteOneOrNoneAggregateMessageConsumer() {
        givenOptionalIdentifierRepositoryListener();
        whenBuildingMessageListener();
        thenMessageConsumerIsDeleteOneOrNone();
    }

    private void givenOptionalIdentifierRepositoryListener() {
        givenRepositoryListener("deleteOptional");
    }

    @Test
    public void severalIdentifiersListenerGetsDeleteSeveralAggregatesMessageConsumer() {
        givenSeveralIdentifiersRepositoryListener();
        whenBuildingMessageListener();
        thenMessageConsumerIsDeleteSeveral();
    }

    private void givenSeveralIdentifiersRepositoryListener() {
        givenRepositoryListener("deleteSeveral");
    }

    private void thenMessageConsumerIsDeleteSeveral() {
        var messageConsumer = listener.consumer();
        assertTrue(messageConsumer instanceof DeleteSeveralAggregatesMessageConsumer);
    }
}
