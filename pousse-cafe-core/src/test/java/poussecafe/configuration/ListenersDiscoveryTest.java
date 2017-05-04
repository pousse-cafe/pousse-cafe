package poussecafe.configuration;

import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.messaging.Queue;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ListenersDiscoveryTest {

    @Mock
    private MessageListenerRegistry registry;

    @Mock
    private StorageServiceLocator storageServiceLocator;

    private WorkflowExplorer workflowExplorer;

    private DummyWorkflow workflow;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        workflowExplorer = new WorkflowExplorer();
        workflowExplorer.setMessageListenerRegistry(registry);
        workflowExplorer.setStorageServiceLocator(storageServiceLocator);
    }

    @Test
    public void discoverDomainEventListenerWithDefaultId() {
        givenWorkflow();
        whenConfiguringWorkflow();
        thenDomainEventListenerWithDefaultIdRegistered();
    }

    private void givenWorkflow() {
        workflow = new DummyWorkflow();
    }

    private void whenConfiguringWorkflow() {
        workflowExplorer.configureWorkflow(workflow);
    }

    private void thenDomainEventListenerWithDefaultIdRegistered() {
        verify(registry).registerDomainEventListener(new MessageListenerEntryBuilder()
                .withSource(Queue.forName("domainEvents"))
                .withMessageClass(TestDomainEvent.class)
                .withListenerId("")
                .withMethod(getMethodByName("domainEventListenerWithDefaultId"))
                .withTarget(workflow)
                .build());
    }

    private Method getMethodByName(String name) {
        for (Method method : DummyWorkflow.class.getMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        throw new RuntimeException("Method not found");
    }

    @Test
    public void discoverDomainEventListenerWithCustomId() {
        givenWorkflow();
        whenConfiguringWorkflow();
        thenDomainEventListenerWithCustomIdRegistered();
    }

    private void thenDomainEventListenerWithCustomIdRegistered() {
        verify(registry).registerDomainEventListener(new MessageListenerEntryBuilder()
                .withSource(Queue.forName("domainEvents"))
                .withMessageClass(TestDomainEvent.class)
                .withListenerId("customDomainEventListenerId")
                .withMethod(getMethodByName("domainEventListenerWithCustomId"))
                .withTarget(workflow)
                .build());
    }

    @Test
    public void discoverCommandListenerWithDefaultId() {
        givenWorkflow();
        whenConfiguringWorkflow();
        thenCommandListenerWithDefaultIdRegistered();
    }

    private void thenCommandListenerWithDefaultIdRegistered() {
        verify(registry).registerCommandListener(new MessageListenerEntryBuilder()
                .withSource(Queue.forName("commands"))
                .withMessageClass(TestCommand.class)
                .withListenerId("")
                .withMethod(getMethodByName("commandListenerWithDefaultId"))
                .withTarget(workflow)
                .build());
    }

    @Test
    public void discoverCommandListenerWithCustomId() {
        givenWorkflow();
        whenConfiguringWorkflow();
        thenCommandListenerWithCustomIdRegistered();
    }

    private void thenCommandListenerWithCustomIdRegistered() {
        verify(registry).registerCommandListener(new MessageListenerEntryBuilder()
                .withSource(Queue.forName("commands"))
                .withMessageClass(AnotherTestCommand.class)
                .withListenerId("customCommandListenerId")
                .withMethod(getMethodByName("commandListenerWithCustomId"))
                .withTarget(workflow)
                .build());
    }

    @Test
    public void onlyFourListenersRegistered() {
        givenWorkflow();
        whenConfiguringWorkflow();
        thenOnlyFourListenersRegistered();
    }

    protected void thenOnlyFourListenersRegistered() {
        verify(registry, times(2)).registerDomainEventListener(any(MessageListenerEntry.class));
        verify(registry, times(2)).registerCommandListener(any(MessageListenerEntry.class));
    }

}
