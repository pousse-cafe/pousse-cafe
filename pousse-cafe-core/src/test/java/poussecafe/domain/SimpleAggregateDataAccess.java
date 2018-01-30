package poussecafe.domain;

import poussecafe.domain.SimpleAggregate.Data;
import poussecafe.inmemory.InMemoryDataAccess;

public class SimpleAggregateDataAccess extends InMemoryDataAccess<SimpleAggregate.Data> {

    @Override
    protected Object extractKey(Data data) {
        return data.id().get();
    }

}
