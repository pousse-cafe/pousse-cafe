package poussecafe.mymodule.adapters;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.mymodule.domain.myaggregate.MyAggregate;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = MyAggregate.class,
    dataImplementation = MyAggregateData.class,
    storageName = InternalStorage.NAME
)
public class MyAggregateInternalDataAccess extends InternalDataAccess<MyAggregateId, MyAggregateData> {

}
