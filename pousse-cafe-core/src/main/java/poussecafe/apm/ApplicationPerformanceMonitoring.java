package poussecafe.apm;

public interface ApplicationPerformanceMonitoring {

    ApmTransaction startTransaction(String name);

    ApmSpan currentSpan();
}
