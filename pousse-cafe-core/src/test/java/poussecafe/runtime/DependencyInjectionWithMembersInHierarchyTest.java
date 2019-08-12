package poussecafe.runtime;

import org.junit.Test;

public class DependencyInjectionWithMembersInHierarchyTest extends DependencyInjectionTest {

    @Test
    public void injectingDependencyWithSetterIsSupported() {
        givenInjectorAndServices();
        givenServiceHierarchyWithMembers();
        whenInjectingDependencies();
        thenServiceHasAllDependencies();
    }

    private void givenServiceHierarchyWithMembers() {
        service = new ServiceChildWithMembers();
    }
}
