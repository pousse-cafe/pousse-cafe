package poussecafe.runtime;

import java.lang.reflect.Method;
import java.util.List;
import org.junit.Test;
import poussecafe.discovery.CustomMessageListenerDiscoverer;
import poussecafe.environment.DeclaredMessageListenerIdBuilder;
import poussecafe.environment.MessageListener;

import static org.junit.Assert.assertTrue;

public class DeclaredListenersDiscoveryTest {

    private DummyProcess service;

    @Test
    public void discoverDomainEventListenerWithDefaultId() {
        givenServiceWithDeclaredListeners();
        whenDiscoveringDeclaredListeners();
        thenDomainEventListenerWithDefaultIdRegistered();
    }

    private void givenServiceWithDeclaredListeners() {
        service = new DummyProcess();
    }

    private void whenDiscoveringDeclaredListeners() {
        workflowExplorer = new CustomMessageListenerDiscoverer.Builder()
                .service(service)
                .build();
        discoveredListeners = workflowExplorer.discoverListeners();
    }

    private CustomMessageListenerDiscoverer workflowExplorer;

    private List<MessageListener> discoveredListeners;

    private void thenDomainEventListenerWithDefaultIdRegistered() {
        assertTrue(discoveredListeners.stream().anyMatch(this::listenerWithDefaultId));
    }

    private boolean listenerWithDefaultId(MessageListener listener) {
        return listener.id().equals(new DeclaredMessageListenerIdBuilder()
                .messageClass(TestDomainEvent.class)
                .method(getMethodByName("domainEventListenerWithDefaultId"))
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
        givenServiceWithDeclaredListeners();
        whenDiscoveringDeclaredListeners();
        thenDomainEventListenerWithCustomIdRegistered();
    }

    private void thenDomainEventListenerWithCustomIdRegistered() {
        assertTrue(discoveredListeners.stream().anyMatch(this::listenerWithCustomId));
    }

    private boolean listenerWithCustomId(MessageListener listener) {
        return listener.id().equals("customDomainEventListenerId");
    }

    @Test
    public void onlyTwoListenersRegistered() {
        givenServiceWithDeclaredListeners();
        whenDiscoveringDeclaredListeners();
        thenOnlyTwoListenersRegistered();
    }

    protected void thenOnlyTwoListenersRegistered() {
        assertTrue(discoveredListeners.size() == 2);
    }

}
