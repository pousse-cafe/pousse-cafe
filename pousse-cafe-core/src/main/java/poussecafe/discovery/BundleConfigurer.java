package poussecafe.discovery;

import java.util.HashSet;
import java.util.Set;
import poussecafe.environment.Bundle;
import poussecafe.environment.BundleDefinition;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.internal.InternalMessaging;
import poussecafe.runtime.MessagingAndStorage;
import poussecafe.storage.Storage;
import poussecafe.storage.internal.InternalStorage;

public class BundleConfigurer {

    public static class Builder {

        private BundleConfigurer configurer = new BundleConfigurer();

        public Builder moduleBasePackage(String packageName) {
            this.moduleBasePackages.add(packageName);
            return this;
        }

        private Set<String> moduleBasePackages = new HashSet<>();

        public BundleConfigurer build() {
            configurer.classPathExplorer = new ClassPathExplorer(moduleBasePackages);
            return configurer;
        }
    }

    private BundleConfigurer() {

    }

    private ClassPathExplorer classPathExplorer;

    public BundleDefinition define() {
        BundleDefinition.Builder builder = new BundleDefinition.Builder();
        builder.withAggregateDefinitions(classPathExplorer.discoverAggregates());
        builder.withDomainProcesses(classPathExplorer.discoverDomainProcesses());
        builder.withServices(classPathExplorer.discoverServices());
        builder.withMessages(classPathExplorer.discoverMessages());
        builder.withMessageListeners(classPathExplorer.discoverListeners());
        return builder.build();
    }

    public Bundle.Builder defineAndImplementDefault() {
        return defineThenImplement()
                .messaging(InternalMessaging.instance())
                .storage(InternalStorage.instance());
    }

    public BundleWithoutImplementation defineThenImplement() {
        Bundle.Builder builder = new Bundle.Builder();
        builder.definition(define());
        builder.serviceImplementations(classPathExplorer.discoverServiceImplementations());
        return new BundleWithoutImplementation(builder);
    }

    public class BundleWithoutImplementation {

        private BundleWithoutImplementation(Bundle.Builder builder) {
            this.builder = builder;
        }

        private Bundle.Builder builder;

        public Bundle.Builder messagingAndStorage(MessagingAndStorage messagingAndStorage) {
            return messaging(messagingAndStorage.messaging())
                    .storage(messagingAndStorage.storage());
        }

        public BundleWithMessaging messaging(Messaging messaging) {
            builder.messagingImplementations(new MessagingImplementationDiscovery.Builder()
                    .classPathExplorer(classPathExplorer)
                    .messaging(messaging)
                    .build()
                    .discover());
            return new BundleWithMessaging(builder);
        }
    }

    public class BundleWithMessaging {

        private BundleWithMessaging(Bundle.Builder builder) {
            this.builder = builder;
        }

        private Bundle.Builder builder;

        public Bundle.Builder storage(Storage storage) {
            builder.storageImplementations(new StorageImplementationDiscovery.Builder()
                    .classPathExplorer(classPathExplorer)
                    .storage(storage)
                    .build()
                    .discover());
            return builder;
        }
    }
}
