package poussecafe.context;

import poussecafe.injector.Injector;

import static org.junit.Assert.assertTrue;

public class DependencyInjectionTest {

    protected void givenInjectorAndServices() {
        injector = new Injector();
        dependency1 = new Service1();
        dependency2 = new Service2();

        injector.registerInjectableService(dependency1);
        injector.registerInjectableService(dependency2);
    }

    protected Injector injector;

    protected Service1 dependency1;

    protected Service2 dependency2;

    protected DependencyAware service;

    protected void whenInjectingDependencies() {
        injector.addInjectionCandidate(service);
        injector.injectDependencies();
    }

    protected void thenServiceHasAllDependencies() {
        assertTrue(service.hasAllDependencies());
    }
}
