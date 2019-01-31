package poussecafe.doc.model.entitydoc;

import poussecafe.contextconfigurer.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = EntityDoc.class,
    dataImplementation = EntityDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalEntityDocDataAccess extends InternalDataAccess<EntityDocKey, EntityDocData> implements EntityDocDataAccess<EntityDocData> {

}
