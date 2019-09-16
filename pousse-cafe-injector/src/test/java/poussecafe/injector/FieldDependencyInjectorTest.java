package poussecafe.injector;

import org.junit.Test;
import poussecafe.injector.other.ServiceWithPackageProtectedFieldDependency;

public class FieldDependencyInjectorTest extends InjectorTest {

    @Test
    public void injectionInPrivateField() {
        givenInjector();
        givenServiceWithDependency(new ServiceWithPrivateFieldDependency());
        whenInjectingDependencies();
        thenInjectionSuccessful();
    }

    @Test
    public void injectionInPackageProtectedField() {
        givenInjector();
        givenServiceWithDependency(new ServiceWithPackageProtectedFieldDependency());
        whenInjectingDependencies();
        thenInjectionSuccessful();
    }

    @Test
    public void injectionInProtectedField() {
        givenInjector();
        givenServiceWithDependency(new ServiceWithProtectedFieldDependency());
        whenInjectingDependencies();
        thenInjectionSuccessful();
    }
}
