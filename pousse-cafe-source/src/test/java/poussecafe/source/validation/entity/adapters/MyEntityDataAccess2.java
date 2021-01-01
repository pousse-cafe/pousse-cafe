package poussecafe.source.validation.entity.adapters;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.source.validation.entity.MyAggregateRoot;
import poussecafe.storage.internal.InternalDataAccess;

@DataAccessImplementation(aggregateRoot = MyAggregateRoot.class, dataImplementation = MyAggregateRootAttributes.class, storageName = "storage2")
public class MyEntityDataAccess2 extends InternalDataAccess<String, MyAggregateRoot.Attributes> {

}
