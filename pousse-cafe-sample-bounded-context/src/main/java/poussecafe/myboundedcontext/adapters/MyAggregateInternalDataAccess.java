package poussecafe.myboundedcontext.adapters;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregate;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregateId;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = MyAggregate.class,
    dataImplementation = MyAggregateData.class,
    storageName = InternalStorage.NAME
)
public class MyAggregateInternalDataAccess extends InternalDataAccess<MyAggregateId, MyAggregateData> {

}
