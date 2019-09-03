package poussecafe.domain.chain;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = ChainElement.class,
    dataImplementation = ChainElementData.class,
    storageName = InternalStorage.NAME
)
public class ChainElementDataAccess extends InternalDataAccess<ChainElementId, ChainElement.Attributes> {

}
