package poussecafe.context;

import java.util.Set;
import poussecafe.domain.EntityDefinition;
import poussecafe.domain.EntityImplementation;
import poussecafe.domain.Service;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageImplementationConfiguration;
import poussecafe.messaging.Messaging;
import poussecafe.process.DomainProcess;
import poussecafe.storage.Storage;

import static poussecafe.check.Checks.checkThatValue;

public abstract class DiscoveredBoundedContext extends BoundedContext {

    public DiscoveredBoundedContext(String packageName) {
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
    protected void loadEntityImplementations(Set<EntityImplementation> implementations) {
        implementations.addAll(storage().newStorageUnit()
                .withPackage(packageName)
                .build()
                .implementations());
    }

    protected abstract Storage storage();

    @Override
    protected void loadProcesses(Set<Class<? extends DomainProcess>> processes) {
        processes.addAll(BoundedContextDiscovery.discoverDomainProcesses(packageName));
    }

    @Override
    protected void loadServices(Set<Class<? extends Service>> services) {
        services.addAll(BoundedContextDiscovery.discoverServices(packageName));
    }

    @Override
    protected void loadMessageImplementations(Set<MessageImplementationConfiguration> implementations) {
        implementations.addAll(messaging().newMessagingUnit()
                .withPackage(packageName)
                .build()
                .implementations());
    }

    protected abstract Messaging messaging();

    @Override
    protected void loadMessages(Set<Class<? extends Message>> messages) {
        messages.addAll(BoundedContextDiscovery.discoverMessages(packageName));
    }
}
