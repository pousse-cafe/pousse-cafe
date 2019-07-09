package poussecafe.discovery;

import java.util.Set;
import org.junit.Test;
import poussecafe.environment.MessageListenerDefinition;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

public class ListenerDiscoveryTest {

    @Test
    public void expectedListenersDiscoveredInDomainProcessHierarchy() throws NoSuchMethodException, SecurityException {
        givenPackageToExplore();
        whenDiscoveringListeners();
        thenExpectedListenersDefined();
    }

    private void givenPackageToExplore() {
        packageToExplore = "poussecafe.discovery";
    }

    private String packageToExplore;

    private void whenDiscoveringListeners() {
        listeners = new ClassPathExplorer(asList(packageToExplore))
                .discoverListeners();
    }

    private Set<MessageListenerDefinition> listeners;

    private void thenExpectedListenersDefined() throws NoSuchMethodException {
        assertTrue(listeners.size() == 4);

        assertTrue(listeners.contains(new MessageListenerDefinition.Builder()
                .containerClass(ListenerContainer1.class)
                .method(ListenerContainer1.class.getMethod("handle", Event1.class))
                .build()));
        assertTrue(listeners.contains(new MessageListenerDefinition.Builder()
                .containerClass(ListenerContainer1.class)
                .method(ListenerContainer1.class.getMethod("handle", Event2.class))
                .build()));

        assertTrue(listeners.contains(new MessageListenerDefinition.Builder()
                .containerClass(ListenerContainer2.class)
                .method(ListenerContainer2.class.getMethod("handle", Event1.class))
                .build()));
        assertTrue(listeners.contains(new MessageListenerDefinition.Builder()
                .containerClass(ListenerContainer2.class)
                .method(ListenerContainer2.class.getMethod("handle", Event3.class))
                .build()));
    }
}
