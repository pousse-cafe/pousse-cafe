package poussecafe.domain;

public class SimpleAggregate extends AggregateRoot<SimpleAggregateKey, SimpleAggregate.Data> {

    public static interface Data extends EntityData<SimpleAggregateKey> {

    }

}
