package poussecafe.testmodule;

import java.util.Optional;
import poussecafe.environment.SecondaryIdentifierHandler;
import poussecafe.listeners.UpdateOneOrNoneRunner;

public class SimpleAggregateTouchByDataRunner extends UpdateOneOrNoneRunner<TestDomainEvent5, String, SimpleAggregate> {

    @Override
    protected Optional<String> aggregateId(TestDomainEvent5 message) {
        String data = message.identifier().value();
        return findFirst(data).map(aggregate -> aggregate.attributes().data().value());
    }

    @Override
    public SecondaryIdentifierHandler<String, SimpleAggregate> secondaryIdentifierHandler() {
        return new SecondaryIdentifierHandler.Builder<String, SimpleAggregate>()
                .aggregateRetriever(this::findFirst)
                .identifierExtractor(aggregate -> aggregate.attributes().data().value())
                .build();
    }

    private Optional<SimpleAggregate> findFirst(String data) {
        var aggregates = repository.findByData(data);
        if(aggregates.size() > 0) {
            return Optional.of(aggregates.get(0));
        } else {
            return Optional.empty();
        }
    }

    private SimpleAggregateRepository repository;
}
