package poussecafe.domain.chain2;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = Chain2Element.class,
    dataImplementation = Chain2ElementData.class,
    storageName = InternalStorage.NAME
)
public class Chain2ElementDataAccess extends InternalDataAccess<Chain2ElementId, Chain2Element.Attributes> {

}
