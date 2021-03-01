package poussecafe.environment;

import org.junit.Test;
import poussecafe.environment.RepositoryListenerTestComponents.Repository;
import poussecafe.environment.RepositoryListenerTestComponents.Root;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DeleteSeveralAggregatesMessageConsumerTest extends DeleteAggregatesMessageConsumerTest {

    @Test
    public void identifiersRepositoryDeletes() {
        givenExistingAggregate();
        givenDeleteSeveral();
        givenDeleteSeveralMessageConsumer();
        whenConsumingState();
        thenRepositoryDelete();
    }

    private void givenDeleteSeveral() {
        givenInvoker(Repository.deleteSeveralMethod());
        var repository = repository();
        when(repository.deleteSeveral(any())).thenReturn(asList(IDENTIFIER));
    }

    private void givenDeleteSeveralMessageConsumer() {
        var environment = environment();
        var apmMocker = new ApplicationPerformanceMonitoringMocker();
        deleteSeveralConsumer = new DeleteSeveralAggregatesMessageConsumer.Builder()
                .listenerId("listenerId")
                .transactionRunnerLocator(TransactionRunnerLocatorMock.mock(Root.class))
                .invoker(invoker())
                .aggregateServices(environment.aggregateServicesOf(Root.class).orElseThrow())
                .applicationPerformanceMonitoring(apmMocker.mock())
                .expectedEvents(emptyList())
                .build();
    }

    private DeleteSeveralAggregatesMessageConsumer deleteSeveralConsumer;

    @Override
    protected MessageConsumer consumer() {
        return deleteSeveralConsumer;
    }
}
