package poussecafe.domain.chain1;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = Chain1Element.class,
    dataImplementation = Chain1ElementData.class,
    storageName = InternalStorage.NAME
)
public class Chain1ElementDataAccess extends InternalDataAccess<Chain1ElementId, Chain1Element.Attributes> {

}
