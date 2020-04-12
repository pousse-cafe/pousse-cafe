package poussecafe.environment;

import org.junit.Test;
import poussecafe.testmodule.SimpleAggregate;
import poussecafe.testmodule.SimpleAggregateId;

import static org.junit.Assert.assertTrue;

public class MessageListenerConsumptionReportTest {

    @Test
    public void reportWithSkippedIds() {
        givenAggregateUpdateReport();
        whenReportingSkippedExecution();
        thenBuildSuccessful();
    }

    private void givenAggregateUpdateReport() {
        aggregateId = new SimpleAggregateId("aggregateId");
        reportBuilder = new MessageListenerConsumptionReport.Builder("listenerId")
                .aggregateType(SimpleAggregate.class)
                .aggregateId(aggregateId);
    }

    private MessageListenerConsumptionReport.Builder reportBuilder;

    private SimpleAggregateId aggregateId;

    private void whenReportingSkippedExecution() {
        reportBuilder.skippedAggregateId(aggregateId);
    }

    private void thenBuildSuccessful() {
        try {
            reportBuilder.build();
            assertTrue(true);
        } catch(Exception e) {
            assertTrue(false);
        }
    }
}
