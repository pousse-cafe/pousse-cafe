package poussecafe.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.apm.ApplicationPerformanceMonitoring;

public abstract class DomainProcess extends TransactionAwareService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    protected ApplicationPerformanceMonitoring applicationPerformanceMonitoring() {
        return applicationPerformanceMonitoring;
    }
}
