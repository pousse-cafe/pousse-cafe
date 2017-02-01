package poussecafe.domain;

public class SimpleAggregateRepository extends Repository<SimpleAggregate, SimpleAggregateKey, SimpleAggregate.Data> {

    @Override
    protected SimpleAggregate newAggregate() {
        return new SimpleAggregate();
    }

}
