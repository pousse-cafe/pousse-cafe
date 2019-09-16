package poussecafe.injector;

public class ServiceWithPrivateFieldDependency implements ServiceWithDependency {

    private InjectableService injectableService;

    @Override
    public boolean isDependencyInjected() {
        return injectableService != null;
    }
}
