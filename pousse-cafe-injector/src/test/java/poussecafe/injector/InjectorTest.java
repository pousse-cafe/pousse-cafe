package poussecafe.injector;

import java.util.Objects;

import static org.junit.Assert.assertTrue;

public abstract class InjectorTest {

    protected void givenInjector() {
        injector = new Injector.Builder()
                .registerInjectableService(new InjectableService())
                .build();
    }

    private Injector injector;

    public void givenServiceWithDependency(ServiceWithDependency service) {
        Objects.requireNonNull(service);
        this.service = service;
    }

    private ServiceWithDependency service;

    protected void whenInjectingDependencies() {
        injector.injectDependenciesInto(service);
    }

    protected void thenInjectionSuccessful() {
        assertTrue(service.isDependencyInjected());
    }
}
