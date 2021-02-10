package poussecafe.source.validation.types.adapters;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.source.validation.types.MyAggregate;
import poussecafe.source.validation.types.MyAggregateDataAccess;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = MyAggregate.Root.class,
    dataImplementation = MyAggregateAttributes.class,
    storageName = InternalStorage.NAME
)
public class MyAggregateInternalDataAccess
extends InternalDataAccess<String, MyAggregateAttributes>
implements MyAggregateDataAccess<MyAggregateAttributes> {

}
