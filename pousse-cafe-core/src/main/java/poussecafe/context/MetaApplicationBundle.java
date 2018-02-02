package poussecafe.context;

import java.util.HashSet;
import java.util.Set;
import poussecafe.journal.JournalEntry;
import poussecafe.journal.JournalEntryFactory;
import poussecafe.journal.JournalEntryRepository;
import poussecafe.journal.memory.InMemoryJournalEntryData;
import poussecafe.journal.memory.InMemoryJournalEntryDataAccess;
import poussecafe.service.DomainProcess;
import poussecafe.storable.StorableDefinition;
import poussecafe.storable.StorableImplementation;
import poussecafe.storage.memory.InMemoryStorage;
import poussecafe.util.IdGenerator;

import static java.util.Collections.unmodifiableSet;

public abstract class MetaApplicationBundle {

    protected MetaApplicationBundle() {
        loadCoreDefinitions(definitions);
        loadDefinitions(definitions);
        loadCoreImplementations(implementations);
        loadImplementations(implementations);
        loadProcesses(processes);
        loadCoreServices(services);
        loadServices(services);
    }

    private Set<StorableDefinition> definitions = new HashSet<>();

    protected void loadCoreDefinitions(Set<StorableDefinition> definitions) {
        definitions.add(new StorableDefinition.Builder()
                .withStorableClass(JournalEntry.class)
                .withFactoryClass(JournalEntryFactory.class)
                .withRepositoryClass(JournalEntryRepository.class)
                .build());
    }

    protected abstract void loadDefinitions(Set<StorableDefinition> definitions);

    private Set<StorableImplementation> implementations = new HashSet<>();

    protected void loadCoreImplementations(Set<StorableImplementation> coreImplementations) {
        coreImplementations.add(new StorableImplementation.Builder()
                .withStorableClass(JournalEntry.class)
                .withDataFactory(InMemoryJournalEntryData::new)
                .withDataAccessFactory(InMemoryJournalEntryDataAccess::new)
                .withStorage(InMemoryStorage.instance())
                .build());
    }

    protected abstract void loadImplementations(Set<StorableImplementation> implementations);

    private Set<Class<? extends DomainProcess>> processes = new HashSet<>();

    protected abstract void loadProcesses(Set<Class<? extends DomainProcess>> processes);

    private Set<Class<?>> services = new HashSet<>();

    protected void loadCoreServices(Set<Class<?>> services) {
        services.add(IdGenerator.class);
    }

    protected abstract void loadServices(Set<Class<?>> services);

    public Set<StorableDefinition> getDefinitions() {
        return unmodifiableSet(definitions);
    }

    public Set<StorableImplementation> getImplementations() {
        return unmodifiableSet(implementations);
    }

    public Set<Class<? extends DomainProcess>> getProcesses() {
        return unmodifiableSet(processes);
    }

    public Set<Class<?>> getServices() {
        return unmodifiableSet(services);
    }
}
