package poussecafe.apm;


public class DefaultApplicationPerformanceMonitoring implements ApplicationPerformanceMonitoring {

    @Override
    public ApmTransaction startTransaction(String name) {
        return new DefaultApmTransaction();
    }
}
