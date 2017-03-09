package poussecafe.configuration;

import java.util.List;
import java.util.function.Function;
import poussecafe.storable.StorableData;
import poussecafe.storage.ConsequenceEmissionPolicy;
import poussecafe.storage.Storage;
import poussecafe.storage.TransactionRunner;

public class StorageServiceLocator {

    private List<Storage> storages;

    private Storage defaultStorage;

    public void setStorages(List<Storage> storages) {
        this.storages = storages;
    }

    public void setDefaultStorage(Storage defaultStorage) {
        this.defaultStorage = defaultStorage;
    }

    public <D extends StorableData<?>> ConsequenceEmissionPolicy locateConsequenceEmissionPolicy(Class<D> dataClass) {
        return locateService(dataClass, p -> p.storage.getConsequenceEmissionPolicy());
    }

    public <K, D extends StorableData<K>> StorageServices<K, D> locateStorageServices(Class<D> dataClass) {
        return locateService(dataClass, p -> p.storage.getStorageServices(dataClass));
    }

    private <S, D extends StorableData<?>> S locateService(Class<D> dataClass,
            Function<ServiceSupplierParameters<D>, S> serviceSupplier) {
        for (Storage storage : storages) {
            ServiceSupplierParameters<D> serviceSupplierParameters = new ServiceSupplierParameters<>();
            serviceSupplierParameters.storage = storage;
            serviceSupplierParameters.dataClass = dataClass;
            if (storage.isStoring(dataClass)) {
                return serviceSupplier.apply(serviceSupplierParameters);
            }
        }

        ServiceSupplierParameters<D> serviceSupplierParameters = new ServiceSupplierParameters<>();
        serviceSupplierParameters.storage = defaultStorage;
        serviceSupplierParameters.dataClass = dataClass;
        return serviceSupplier.apply(serviceSupplierParameters);
    }

    public <D extends StorableData<?>> TransactionRunner locateTransactionRunner(Class<D> dataClass) {
        return locateService(dataClass, p -> p.storage.getTransactionRunner());
    }

    private class ServiceSupplierParameters<D extends StorableData<?>> {

        Storage storage;

        Class<D> dataClass;
    }

}
