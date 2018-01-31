package poussecafe.context;

public class ServiceWithPublicMembers implements DependencyAware {

    public Service1 service1;

    public Service2 service2;

    @Override
    public boolean hasAllDependencies() {
        return service1 != null && service2 != null;
    }

}
