package poussecafe.context;

import poussecafe.injector.Injector;

import static org.junit.Assert.assertTrue;

public abstract class DependencyInjectionTest {

    protected void givenInjectorAndServices() {
        dependency1 = new Service1();
        dependency2 = new Service2();

        injector = new Injector.Builder()
                .registerInjectableService(dependency1)
                .registerInjectableService(dependency2)
                .build();
    }

    protected Injector injector;

    protected Service1 dependency1;

    protected Service2 dependency2;

    protected DependencyAware service;

    protected void whenInjectingDependencies() {
        injector.injectDependenciesInto(service);
    }

    protected void thenServiceHasAllDependencies() {
        assertTrue(service.hasAllDependencies());
    }
}
