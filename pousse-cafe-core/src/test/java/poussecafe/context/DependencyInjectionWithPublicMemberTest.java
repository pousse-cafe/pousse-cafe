package poussecafe.context;

import org.junit.Test;

public class DependencyInjectionWithPublicMemberTest extends DependencyInjectionTest {

    @Test
    public void injectingDependencyWithSetterIsSupported() {
        givenInjectorAndServices();
        givenServiceWithPublicMembers();
        whenInjectingDependencies();
        thenServiceHasAllDependencies();
    }

    private void givenServiceWithPublicMembers() {
        service = new ServiceWithPublicMembers();
    }
}
