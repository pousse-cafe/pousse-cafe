package poussecafe.contextconfigurer;

import java.util.HashSet;
import java.util.Set;
import poussecafe.context.BoundedContext;
import poussecafe.context.BoundedContextDefinition;
import poussecafe.context.MessagingAndStorage;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.internal.InternalMessaging;
import poussecafe.storage.Storage;
import poussecafe.storage.internal.InternalStorage;

public class BoundedContextConfigurer {

    public static class Builder {

        private BoundedContextConfigurer configurer = new BoundedContextConfigurer();

        public Builder packagePrefix(String packageName) {
            this.packageName.add(packageName);
            return this;
        }

        private Set<String> packageName = new HashSet<>();

        public BoundedContextConfigurer build() {
            configurer.classPathExplorer = new ClassPathExplorer(packageName);
            return configurer;
        }
    }

    private BoundedContextConfigurer() {

    }

    private ClassPathExplorer classPathExplorer;

    public BoundedContextDefinition define() {
        BoundedContextDefinition.Builder builder = new BoundedContextDefinition.Builder();
        builder.withAggregateDefinitions(classPathExplorer.discoverDefinitions());
        builder.withDomainProcesses(classPathExplorer.discoverDomainProcesses());
        builder.withServices(classPathExplorer.discoverServices());
        builder.withMessages(classPathExplorer.discoverMessages());
        builder.withMessageListeners(classPathExplorer.discoverListeners());
        return builder.build();
    }

    public BoundedContext.Builder defineAndImplementDefault() {
        return defineThenImplement()
                .messaging(InternalMessaging.instance())
                .storage(InternalStorage.instance());
    }

    public BoundedContextWithNoImplementation defineThenImplement() {
        BoundedContext.Builder builder = new BoundedContext.Builder();
        builder.definition(define());
        builder.serviceImplementations(classPathExplorer.discoverServiceImplementations());
        return new BoundedContextWithNoImplementation(builder);
    }

    public class BoundedContextWithNoImplementation {

        private BoundedContextWithNoImplementation(BoundedContext.Builder builder) {
            this.builder = builder;
        }

        private BoundedContext.Builder builder;

        public BoundedContext.Builder messagingAndStorage(MessagingAndStorage messagingAndStorage) {
            return messaging(messagingAndStorage.messaging())
                    .storage(messagingAndStorage.storage());
        }

        public BoundedContextWithMessaging messaging(Messaging messaging) {
            builder.messagingImplementations(new MessagingImplementationDiscovery.Builder()
                    .classPathExplorer(classPathExplorer)
                    .messaging(messaging)
                    .build()
                    .discover());
            return new BoundedContextWithMessaging(builder);
        }
    }

    public class BoundedContextWithMessaging {

        private BoundedContextWithMessaging(BoundedContext.Builder builder) {
            this.builder = builder;
        }

        private BoundedContext.Builder builder;

        public BoundedContext.Builder storage(Storage storage) {
            builder.storageImplementations(new StorageImplementationDiscovery.Builder()
                    .classPathExplorer(classPathExplorer)
                    .storage(storage)
                    .build()
                    .discover());
            return builder;
        }
    }
}
