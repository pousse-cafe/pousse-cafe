package poussecafe.doc.model.vodoc;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = ValueObjectDoc.class,
    dataImplementation = ValueObjectDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalValueObjectDocDataAccess extends InternalDataAccess<ValueObjectDocKey, ValueObjectDocData> implements ValueObjectDocDataAccess<ValueObjectDocData> {

}
