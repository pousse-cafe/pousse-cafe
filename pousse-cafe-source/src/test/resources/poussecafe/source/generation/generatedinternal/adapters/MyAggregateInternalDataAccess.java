package poussecafe.source.generation.generatedinternal.adapters;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.source.generation.generatedinternal.MyAggregate;
import poussecafe.source.generation.generatedinternal.MyAggregateDataAccess;
import poussecafe.source.generation.generatedinternal.MyAggregateId;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = MyAggregate.class,
    dataImplementation = MyAggregateAttributes.class,
    storageName = InternalStorage.NAME
)
public class MyAggregateInternalDataAccess extends InternalDataAccess<MyAggregateId, MyAggregateAttributes> implements
        MyAggregateDataAccess<MyAggregateAttributes> {

    public MyAggregateInternalDataAccess() {
        versionField("version");
    }
}