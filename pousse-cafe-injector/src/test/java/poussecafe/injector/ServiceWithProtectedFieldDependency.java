package poussecafe.injector;

public class ServiceWithProtectedFieldDependency implements ServiceWithDependency {

    protected InjectableService injectableService;

    @Override
    public boolean isDependencyInjected() {
        return injectableService != null;
    }
}
