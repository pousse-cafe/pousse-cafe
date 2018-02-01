package poussecafe.context;

import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import poussecafe.messaging.MessageListenerRegistry;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ListenersDiscoveryTest {

    @Mock
    private MessageListenerRegistry registry;

    private ProcessExplorer workflowExplorer;

    private DummyProcess workflow;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        workflowExplorer = new ProcessExplorer();
        workflowExplorer.setMessageListenerRegistry(registry);
    }

    @Test
    public void discoverDomainEventListenerWithDefaultId() {
        givenWorkflow();
        whenConfiguringWorkflow();
        thenDomainEventListenerWithDefaultIdRegistered();
    }

    private void givenWorkflow() {
        workflow = new DummyProcess();
    }

    private void whenConfiguringWorkflow() {
        workflowExplorer.discoverListeners(workflow);
    }

    private void thenDomainEventListenerWithDefaultIdRegistered() {
        verify(registry).registerListener(new MessageListenerEntryBuilder()
                .withMessageClass(TestDomainEvent.class)
                .withListenerId("")
                .withMethod(getMethodByName("domainEventListenerWithDefaultId"))
                .withTarget(workflow)
                .build());
    }

    private Method getMethodByName(String name) {
        for (Method method : DummyProcess.class.getMethods()) {
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
        verify(registry).registerListener(new MessageListenerEntryBuilder()
                .withMessageClass(TestDomainEvent.class)
                .withListenerId("customDomainEventListenerId")
                .withMethod(getMethodByName("domainEventListenerWithCustomId"))
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
        verify(registry, times(2)).registerListener(any(MessageListenerEntry.class));
    }

}
