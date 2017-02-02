package poussecafe.sample.app;

import org.springframework.context.annotation.Configuration;
import poussecafe.configuration.ConsequenceJournalEntryConfiguration;
import poussecafe.configuration.InMemoryConsequenceJournalEntryConfiguration;
import poussecafe.configuration.StorageConfiguration;
import poussecafe.spring.SpringMetaApplicationConfiguration;
import poussecafe.storage.TransactionLessStorage;

@Configuration
public class PousseCafeConfiguration extends SpringMetaApplicationConfiguration {

    @Override
    public StorageConfiguration storageConfiguration() {
        return new TransactionLessStorage();
    }

    @Override
    protected ConsequenceJournalEntryConfiguration consequenceJournalEntryConfiguration() {
        return new InMemoryConsequenceJournalEntryConfiguration();
    }

}
