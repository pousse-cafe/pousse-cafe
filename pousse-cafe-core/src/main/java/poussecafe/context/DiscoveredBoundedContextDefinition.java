package poussecafe.context;

import java.util.Set;
import poussecafe.domain.EntityDefinition;
import poussecafe.domain.Service;
import poussecafe.messaging.Message;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.internal.InternalMessaging;
import poussecafe.process.DomainProcess;
import poussecafe.storage.Storage;
import poussecafe.storage.internal.InternalStorage;

import static poussecafe.check.Checks.checkThatValue;

public abstract class DiscoveredBoundedContextDefinition extends BoundedContextDefinition {

    public DiscoveredBoundedContextDefinition(String packageName) {
        super(false);

        packageName(packageName);
        loadAll();
    }

    private void packageName(String packageName) {
        checkThatValue(packageName).notNull();
        this.packageName = packageName;
    }

    private String packageName;

    @Override
    protected void loadDefinitions(Set<EntityDefinition> definitions) {
        definitions.addAll(BoundedContextDiscovery.discoverDefinitions(packageName));
    }

    @Override
    protected void loadProcesses(Set<Class<? extends DomainProcess>> processes) {
        processes.addAll(BoundedContextDiscovery.discoverDomainProcesses(packageName));
    }

    @Override
    protected void loadServices(Set<Class<? extends Service>> services) {
        services.addAll(BoundedContextDiscovery.discoverServices(packageName));
    }

    @Override
    protected void loadMessages(Set<Class<? extends Message>> messages) {
        messages.addAll(BoundedContextDiscovery.discoverMessages(packageName));
    }

    public BoundedContext.Builder withDefaultImplementation() {
        return implement()
                .messaging(InternalMessaging.instance())
                .storage(InternalStorage.instance());
    }

    public BoundedContextWithNoImplementation implement() {
        BoundedContext.Builder builder = new BoundedContext.Builder();
        builder.definition(this);
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
                    .packageName(packageName)
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
                    .packageName(packageName)
                    .storage(storage)
                    .build()
                    .discover());
            return builder;
        }
    }
}
