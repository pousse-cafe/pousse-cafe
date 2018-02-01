package poussecafe.domain;

import poussecafe.storable.IdentifiedStorableData;

public class SimpleAggregate extends AggregateRoot<SimpleAggregateKey, SimpleAggregate.Data> {

    public static interface Data extends IdentifiedStorableData<SimpleAggregateKey> {

    }

}
