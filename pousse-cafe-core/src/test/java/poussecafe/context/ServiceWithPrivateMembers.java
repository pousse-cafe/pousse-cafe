package poussecafe.context;

public class ServiceWithPrivateMembers implements DependencyAware {

    private Service1 service1;

    private Service2 service2;

    @Override
    public boolean hasAllDependencies() {
        return service1 != null && service2 != null;
    }

}
