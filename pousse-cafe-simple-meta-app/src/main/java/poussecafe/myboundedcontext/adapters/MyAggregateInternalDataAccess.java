package poussecafe.myboundedcontext.adapters;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.myboundedcontext.domain.MyAggregate;
import poussecafe.myboundedcontext.domain.MyAggregateKey;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = MyAggregate.class,
    dataImplementation = MyAggregateData.class,
    storageName = InternalStorage.NAME
)
public class MyAggregateInternalDataAccess extends InternalDataAccess<MyAggregateKey, MyAggregateData> {

}
