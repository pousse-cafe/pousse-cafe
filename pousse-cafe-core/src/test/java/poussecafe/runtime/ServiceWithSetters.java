package poussecafe.runtime;

public class ServiceWithSetters implements DependencyAware {

    private Service1 service1;

    private Service2 service2;

    public void setService1(Service1 service1) {
        this.service1 = service1;
    }

    public void setService2(Service2 service2) {
        this.service2 = service2;
    }

    @Override
    public boolean hasAllDependencies() {
        return service1 != null && service2 != null;
    }
}
