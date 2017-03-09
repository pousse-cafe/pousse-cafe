package poussecafe.storage;

import poussecafe.configuration.StorageServices;
import poussecafe.storable.StorableData;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableDataFactory;

public interface Storage {

    ConsequenceEmissionPolicy getConsequenceEmissionPolicy();

    TransactionRunner getTransactionRunner();

    <K, D extends StorableData<K>> StorableDataAccess<K, D> getStorableDataAccess(Class<D> dataClass);

    <D extends StorableData<?>> StorableDataFactory<D> getStorableDataFactory(Class<D> dataClass);

    <D extends StorableData<?>> boolean isStoring(Class<D> dataClass);

    <K, D extends StorableData<K>> StorageServices<K, D> getStorageServices(Class<D> dataClass);
}
