package poussecafe.context;

import java.util.List;
import java.util.Objects;
import poussecafe.domain.EntityImplementation;
import poussecafe.storage.Storage;

public class StorageImplementationDiscovery {

    public static class Builder {

        private StorageImplementationDiscovery discovery = new StorageImplementationDiscovery();

        public Builder packageName(String packageName) {
            discovery.packageName = packageName;
            return this;
        }

        public Builder storage(Storage storage) {
            discovery.storage = storage;
            return this;
        }

        public StorageImplementationDiscovery build() {
            Objects.requireNonNull(discovery.packageName);
            Objects.requireNonNull(discovery.storage);
            return discovery;
        }
    }

    private StorageImplementationDiscovery() {

    }

    private String packageName;

    public String packageName() {
        return packageName;
    }

    private Storage storage;

    public Storage storage() {
        return storage;
    }

    public List<EntityImplementation> discover() {
        return storage().newStorageUnit()
                .withPackage(packageName)
                .build()
                .implementations();
    }
}
