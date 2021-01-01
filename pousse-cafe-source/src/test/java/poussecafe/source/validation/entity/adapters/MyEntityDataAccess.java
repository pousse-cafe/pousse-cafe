package poussecafe.source.validation.entity.adapters;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.source.validation.entity.MyAggregateRoot;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(aggregateRoot = MyAggregateRoot.class, dataImplementation = MyAggregateRootAttributes.class, storageName = InternalStorage.NAME)
public class MyEntityDataAccess extends InternalDataAccess<String, MyAggregateRoot.Attributes> {

}
