package poussecafe.discovery;

import java.util.Set;
import org.junit.Test;
import poussecafe.environment.MessageListenerDefinition;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

public class DomainProcessHierarchyListenerDiscoveryTest {

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
        assertTrue(listeners.contains(new MessageListenerDefinition.Builder()
                .containerClass(DomainProcess1.class)
                .method(DomainProcess1.class.getMethod("handle", Event1.class))
                .build()));
        assertTrue(listeners.contains(new MessageListenerDefinition.Builder()
                .containerClass(DomainProcess1.class)
                .method(DomainProcess1.class.getMethod("handle", Event2.class))
                .build()));

        assertTrue(listeners.contains(new MessageListenerDefinition.Builder()
                .containerClass(DomainProcess2.class)
                .method(DomainProcess2.class.getMethod("handle", Event1.class))
                .build()));
        assertTrue(listeners.contains(new MessageListenerDefinition.Builder()
                .containerClass(DomainProcess2.class)
                .method(DomainProcess2.class.getMethod("handle", Event3.class))
                .build()));
    }
}
