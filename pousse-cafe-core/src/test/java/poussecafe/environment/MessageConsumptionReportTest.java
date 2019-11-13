package poussecafe.environment;

import org.junit.Test;
import poussecafe.domain.SimpleAggregate;
import poussecafe.domain.SimpleAggregateId;

import static org.junit.Assert.assertTrue;

public class MessageConsumptionReportTest {

    @Test
    public void reportWithSkippedIds() {
        givenAggregateUpdateReport();
        whenReportingSkippedExecution();
        thenBuildSuccessful();
    }

    private void givenAggregateUpdateReport() {
        aggregateId = new SimpleAggregateId("aggregateId");
        reportBuilder = new MessageConsumptionReport.Builder()
                .aggregateType(SimpleAggregate.class)
                .aggregateId(aggregateId);
    }

    private MessageConsumptionReport.Builder reportBuilder;

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
