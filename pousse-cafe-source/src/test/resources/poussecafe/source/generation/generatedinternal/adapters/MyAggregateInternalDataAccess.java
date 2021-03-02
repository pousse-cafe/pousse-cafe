package poussecafe.source.generation.generatedinternal.adapters;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.source.generation.generatedinternal.MyAggregateId;
import poussecafe.source.generation.generatedinternal.MyAggregateRepository.DataAccess;
import poussecafe.source.generation.generatedinternal.MyAggregateRoot;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = MyAggregateRoot.class,
    dataImplementation = MyAggregateAttributes.class,
    storageName = InternalStorage.NAME
)
public class MyAggregateInternalDataAccess extends InternalDataAccess<MyAggregateId, MyAggregateAttributes> implements
        DataAccess<MyAggregateAttributes> {

    public MyAggregateInternalDataAccess() {
        versionField("version");
    }
}