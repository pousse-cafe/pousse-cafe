package poussecafe.injector.other;

import poussecafe.injector.InjectableService;
import poussecafe.injector.ServiceWithDependency;

public class ServiceWithPackageProtectedFieldDependency implements ServiceWithDependency {

    InjectableService injectableService;

    @Override
    public boolean isDependencyInjected() {
        return injectableService != null;
    }
}
