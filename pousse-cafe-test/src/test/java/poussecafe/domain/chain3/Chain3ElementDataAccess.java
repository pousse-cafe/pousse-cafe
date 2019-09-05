package poussecafe.domain.chain3;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = Chain3Element.class,
    dataImplementation = Chain3ElementData.class,
    storageName = InternalStorage.NAME
)
public class Chain3ElementDataAccess extends InternalDataAccess<Chain3ElementId, Chain3Element.Attributes> {

}
