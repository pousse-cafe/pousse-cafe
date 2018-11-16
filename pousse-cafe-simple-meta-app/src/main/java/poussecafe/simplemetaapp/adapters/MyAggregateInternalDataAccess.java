package poussecafe.simplemetaapp.adapters;

import poussecafe.simplemetaapp.domain.MyAggregate;
import poussecafe.simplemetaapp.domain.MyAggregateKey;
import poussecafe.storage.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = MyAggregate.class,
    dataImplementation = MyAggregateData.class,
    storageName = InternalStorage.NAME
)
public class MyAggregateInternalDataAccess extends InternalDataAccess<MyAggregateKey, MyAggregateData> {

}
