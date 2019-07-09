package poussecafe.discovery;

import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CustomListenerHierarchyDiscoveryTest {

    @Test
    public void expectedListenersDiscoveredInDomainProcessHierarchy() throws NoSuchMethodException, SecurityException {
        givenCustomListenerContainerInstance();
        whenDiscoveringListeners();
        thenExpectedListenersDiscovered();
    }

    private void givenCustomListenerContainerInstance() {
        container = new CustomListenerContainer();
    }

    private Object container;

    private void whenDiscoveringListeners() {
        CustomMessageListenerDiscoverer explorer = new CustomMessageListenerDiscoverer.Builder()
                .service(container)
                .build();
        listeners = explorer.discoverListeners();
    }

    private List<poussecafe.environment.MessageListener> listeners;

    private void thenExpectedListenersDiscovered() throws NoSuchMethodException {
        assertTrue(listeners.size() == 2);
        assertTrue(listeners.stream()
            .map(poussecafe.environment.MessageListener::id)
            .allMatch(id -> id.startsWith(container.getClass().getName())));
    }
}
