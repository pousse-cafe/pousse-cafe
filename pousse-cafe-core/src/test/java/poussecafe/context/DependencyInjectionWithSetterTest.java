package poussecafe.context;

import org.junit.Test;

public class DependencyInjectionWithSetterTest extends DependencyInjectionTest {

    @Test
    public void injectingDependencyWithSetterIsSupported() {
        givenInjectorAndServices();
        givenServiceWithSetters();
        whenInjectingDependencies();
        thenServiceHasAllDependencies();
    }

    private void givenServiceWithSetters() {
        service = new ServiceWithSetters();
    }
}
