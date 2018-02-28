package poussecafe.domain;

import poussecafe.storable.ActiveStorableData;

public class SimpleAggregate extends AggregateRoot<SimpleAggregateKey, SimpleAggregate.Data> {

    public static interface Data extends ActiveStorableData<SimpleAggregateKey> {

    }

}
