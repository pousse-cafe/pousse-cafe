package poussecafe.runtime;

import org.junit.Test;

public class DependencyInjectionWithPrivateMemberTest extends DependencyInjectionTest {

    @Test
    public void injectingDependencyWithSetterIsSupported() {
        givenInjectorAndServices();
        givenServiceWithPrivateMembers();
        whenInjectingDependencies();
        thenServiceHasAllDependencies();
    }

    private void givenServiceWithPrivateMembers() {
        service = new ServiceWithPrivateMembers();
    }
}
