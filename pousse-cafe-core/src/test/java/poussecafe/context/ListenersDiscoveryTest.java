package poussecafe.context;

import java.lang.reflect.Method;
import java.util.List;
import org.junit.Test;
import poussecafe.messaging.MessageListener;

import static org.junit.Assert.assertTrue;

public class ListenersDiscoveryTest {

    private MessageListenersDiscoverer workflowExplorer = new MessageListenersDiscoverer();

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
        discoveredListeners = workflowExplorer.discoverDeclaredListeners(service);
    }

    private List<MessageListener> discoveredListeners;

    private void thenDomainEventListenerWithDefaultIdRegistered() {
        assertTrue(discoveredListeners.stream().anyMatch(this::listenerWithDefaultId));
    }

    private boolean listenerWithDefaultId(MessageListener listener) {
        return listener.id().equals(new DeclaredMessageListenerIdBuilder()
                .messageClass(TestDomainEvent.class)
                .target(service)
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
