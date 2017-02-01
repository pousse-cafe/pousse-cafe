package poussecafe.configuration;

import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import poussecafe.consequence.ConsequenceListenerRegistry;
import poussecafe.consequence.Source;
import poussecafe.storage.TransactionRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ListenersDiscoveryTest {

    @Mock
    private ConsequenceListenerRegistry registry;

    @Mock
    private TransactionRunner transactionRunner;

    private WorkflowExplorer workflowExplorer;

    private DummyWorkflow workflow;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        workflowExplorer = new WorkflowExplorer();
        workflowExplorer.setConsequenceListenerRegistry(registry);
        workflowExplorer.setTransactionRunner(transactionRunner);
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
        verify(registry).registerDomainEventListener(new ConsequenceListenerEntryBuilder()
                .withSource(Source.forName("domainEvents"))
                .withConsequenceClass(TestDomainEvent.class)
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
        verify(registry).registerDomainEventListener(new ConsequenceListenerEntryBuilder()
                .withSource(Source.forName("domainEvents"))
                .withConsequenceClass(TestDomainEvent.class)
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
        verify(registry).registerCommandListener(new ConsequenceListenerEntryBuilder()
                .withSource(Source.forName("commands"))
                .withConsequenceClass(TestCommand.class)
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
        verify(registry).registerCommandListener(new ConsequenceListenerEntryBuilder()
                .withSource(Source.forName("commands"))
                .withConsequenceClass(AnotherTestCommand.class)
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
        verify(registry, times(2)).registerDomainEventListener(any(ConsequenceListenerEntry.class));
        verify(registry, times(2)).registerCommandListener(any(ConsequenceListenerEntry.class));
    }

}
