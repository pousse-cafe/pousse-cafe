package poussecafe.simplemetaapp.adapters;

import poussecafe.simplemetaapp.domain.MyAggregate;
import poussecafe.simplemetaapp.domain.MyAggregateKey;
import poussecafe.storage.DataAccessImplementation;
import poussecafe.storage.memory.InMemoryDataAccess;
import poussecafe.storage.memory.InMemoryStorage;

@DataAccessImplementation(
    aggregateRoot = MyAggregate.class,
    dataImplementation = MyAggregateData.class,
    storageName = InMemoryStorage.NAME
)
public class MyAggregateInMemoryDataAccess extends InMemoryDataAccess<MyAggregateKey, MyAggregateData> {

}
