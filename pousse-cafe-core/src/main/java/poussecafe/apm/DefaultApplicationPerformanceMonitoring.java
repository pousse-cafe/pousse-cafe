package poussecafe.apm;


public class DefaultApplicationPerformanceMonitoring implements ApplicationPerformanceMonitoring {

    @Override
    public ApmTransaction startTransaction() {
        return new DefaultApmTransaction();
    }
}
