package poussecafe.testmodule;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = SimpleAggregate.class,
    dataImplementation = SimpleAggregateData.class,
    storageName = InternalStorage.NAME
)
public class SimpleAggregateDataAccess extends InternalDataAccess<SimpleAggregateId, SimpleAggregate.Attributes> {

}
