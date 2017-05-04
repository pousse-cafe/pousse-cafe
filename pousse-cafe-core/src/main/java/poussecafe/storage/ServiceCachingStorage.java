package poussecafe.storage;

import java.util.HashMap;
import java.util.Map;
import poussecafe.configuration.StorageServices;
import poussecafe.storable.StorableData;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableDataFactory;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class ServiceCachingStorage implements Storage {

    private MessageSendingPolicy policy;

    private TransactionRunner transactionRunner;

    private Map<Class, StorableDataAccess> dataAccesses = new HashMap<>();

    private Map<Class, StorableDataFactory> dataFactories = new HashMap<>();

    public ServiceCachingStorage() {
        policy = newMessageSendingPolicy();
        transactionRunner = newTransactionRunner();
    }

    protected abstract MessageSendingPolicy newMessageSendingPolicy();

    protected abstract TransactionRunner newTransactionRunner();

    @Override
    public <K, D extends StorableData<K>> StorageServices<K, D> getStorageServices(Class<D> dataClass) {
        StorableDataAccess<K, D> dataAccess = getStorableDataAccess(dataClass);
        StorableDataFactory<D> dataFactory = getStorableDataFactory(dataClass);
        return new StorageServices<>(policy, transactionRunner, dataAccess, dataFactory);
    }

    @Override
    public MessageSendingPolicy getMessageSendingPolicy() {
        return policy;
    }

    @Override
    public TransactionRunner getTransactionRunner() {
        return transactionRunner;
    }

    @Override
    public <K, D extends StorableData<K>> StorableDataAccess<K, D> getStorableDataAccess(Class<D> dataClass) {
        StorableDataAccess<K, D> dataAccess = dataAccesses.get(dataClass);
        if (dataAccess == null) {
            dataAccess = newDataAccess(dataClass);
        }
        return dataAccess;
    }

    protected abstract <K, D extends StorableData<K>> StorableDataAccess<K, D> newDataAccess(Class<D> dataClass);

    @Override
    public <D extends StorableData<?>> StorableDataFactory<D> getStorableDataFactory(Class<D> dataClass) {
        StorableDataFactory<D> dataFactory = dataFactories.get(dataClass);
        if (dataFactory == null) {
            dataFactory = newDataFactory(dataClass);
        }
        return dataFactory;
    }

    protected abstract <D extends StorableData<?>> StorableDataFactory<D> newDataFactory(Class<D> dataClass);

    public <K, D extends StorableData<K>> void registerDataAccess(Class<D> dataClass,
            StorableDataAccess<K, D> dataAccess) {
        dataAccesses.put(dataClass, dataAccess);
    }

    public <K, D extends StorableData<K>> void registerDataFactory(Class<D> dataClass,
            StorableDataFactory<D> dataFactory) {
        dataFactories.put(dataClass, dataFactory);
    }

}
