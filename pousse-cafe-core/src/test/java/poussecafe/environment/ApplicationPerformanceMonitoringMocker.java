package poussecafe.environment;

import org.mockito.Mockito;
import poussecafe.apm.ApmSpan;
import poussecafe.apm.ApplicationPerformanceMonitoring;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ApplicationPerformanceMonitoringMocker {

    public ApplicationPerformanceMonitoringMocker() {
        applicationPerformanceMonitoring = Mockito.mock(ApplicationPerformanceMonitoring.class);

        ApmSpan currentSpan = Mockito.mock(ApmSpan.class);
        when(applicationPerformanceMonitoring.currentSpan()).thenReturn(currentSpan);

        newSpan = Mockito.mock(ApmSpan.class);
        when(currentSpan.startSpan()).thenReturn(newSpan);
    }

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    public ApplicationPerformanceMonitoring mock() {
        return applicationPerformanceMonitoring;
    }

    private ApmSpan newSpan;

    public void verifySpanEnded() {
        verify(newSpan).end();
    }
}
