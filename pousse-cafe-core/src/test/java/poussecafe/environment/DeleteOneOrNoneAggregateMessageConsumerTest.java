package poussecafe.environment;

import java.util.Optional;
import org.junit.Test;
import poussecafe.environment.RepositoryListenerTestComponents.Repository;
import poussecafe.environment.RepositoryListenerTestComponents.Root;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DeleteOneOrNoneAggregateMessageConsumerTest extends DeleteAggregatesMessageConsumerTest {

    @Test
    public void identifierValueRepositoryDeletes() {
        givenExistingAggregate();
        givenDeleteSingle();
        givenDeleteOneOrNoneMessageConsumer();
        whenConsumingState();
        thenRepositoryDelete();
    }

    private void givenDeleteSingle() {
        givenInvoker(Repository.deleteSingleMethod());
        var repository = repository();
        when(repository.deleteSingle(any())).thenReturn(IDENTIFIER);
    }

    private void givenDeleteOneOrNoneMessageConsumer() {
        var environment = environment();
        var apmMocker = new ApplicationPerformanceMonitoringMocker();
        deleteOneOrNoneConsumer = new DeleteOneOrNoneAggregateMessageConsumer.Builder()
                .listenerId("listenerId")
                .transactionRunnerLocator(TransactionRunnerLocatorMock.mock(Root.class))
                .invoker(invoker())
                .aggregateServices(environment.aggregateServicesOf(Root.class).orElseThrow())
                .applicationPerformanceMonitoring(apmMocker.mock())
                .expectedEvents(emptyList())
                .build();
    }

    private DeleteOneOrNoneAggregateMessageConsumer deleteOneOrNoneConsumer;

    @Override
    protected MessageConsumer consumer() {
        return deleteOneOrNoneConsumer;
    }

    @Test
    public void optionalIdentifierRepositoryDeletes() {
        givenExistingAggregate();
        givenDeleteOptional();
        givenDeleteOneOrNoneMessageConsumer();
        whenConsumingState();
        thenRepositoryDelete();
    }

    private void givenDeleteOptional() {
        givenInvoker(Repository.deleteOptionalMethod());
        var repository = repository();
        when(repository.deleteOptional(any())).thenReturn(Optional.of(IDENTIFIER));
    }
}
