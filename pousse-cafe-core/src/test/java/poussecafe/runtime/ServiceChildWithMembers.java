package poussecafe.runtime;

public class ServiceChildWithMembers extends ServiceParentWithMembers {

    public Service2 service2;

    @Override
    public boolean hasAllDependencies() {
        return service1 != null && service2 != null;
    }

}
