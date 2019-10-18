package poussecafe.apm;


public class DefaultApplicationPerformanceMonitoring implements ApplicationPerformanceMonitoring {

    @Override
    public ApmTransaction startTransaction(String name) {
        return DefaultApmTransaction.instance();
    }

    @Override
    public ApmSpan currentSpan() {
        return DefaultApmTransaction.instance();
    }
}
