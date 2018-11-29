package poussecafe.context;

import java.util.List;
import java.util.Objects;
import poussecafe.domain.EntityImplementation;
import poussecafe.storage.Storage;

public class StorageImplementationDiscovery {

    public static class Builder {

        private StorageImplementationDiscovery discovery = new StorageImplementationDiscovery();

        public Builder classPathExplorer(ClassPathExplorer classPathExplorer) {
            discovery.classPathExplorer = classPathExplorer;
            return this;
        }

        public Builder storage(Storage storage) {
            discovery.storage = storage;
            return this;
        }

        public StorageImplementationDiscovery build() {
            Objects.requireNonNull(discovery.classPathExplorer);
            Objects.requireNonNull(discovery.storage);
            return discovery;
        }
    }

    private StorageImplementationDiscovery() {

    }

    private ClassPathExplorer classPathExplorer;

    public ClassPathExplorer classPathExplorer() {
        return classPathExplorer;
    }

    private Storage storage;

    public Storage storage() {
        return storage;
    }

    public List<EntityImplementation> discover() {
        return storage().newStorageUnit()
                .classPathExplorer(classPathExplorer)
                .build()
                .implementations();
    }
}
