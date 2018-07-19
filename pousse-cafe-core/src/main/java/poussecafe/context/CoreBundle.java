package poussecafe.context;

import java.util.Set;
import poussecafe.domain.Service;
import poussecafe.journal.JournalEntry;
import poussecafe.journal.JournalEntryFactory;
import poussecafe.journal.JournalEntryRepository;
import poussecafe.process.DomainProcess;
import poussecafe.storable.StorableDefinition;
import poussecafe.util.IdGenerator;

public abstract class CoreBundle extends MetaApplicationBundle {

    @Override
    protected void loadDefinitions(Set<StorableDefinition> definitions) {
        definitions.add(new StorableDefinition.Builder()
                .withStorableClass(JournalEntry.class)
                .withFactoryClass(JournalEntryFactory.class)
                .withRepositoryClass(JournalEntryRepository.class)
                .build());
    }

    @Override
    protected void loadProcesses(Set<Class<? extends DomainProcess>> processes) {
        // No core process
    }

    @Override
    protected void loadServices(Set<Class<? extends Service>> services) {
        services.add(IdGenerator.class);
    }

}
