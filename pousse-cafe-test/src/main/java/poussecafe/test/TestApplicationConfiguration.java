package poussecafe.test;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import poussecafe.configuration.ActiveStorableConfiguration;
import poussecafe.configuration.ApplicationConfiguration;
import poussecafe.configuration.ConsequenceJournalEntryConfiguration;
import poussecafe.configuration.InMemoryConsequenceJournalEntryConfiguration;
import poussecafe.configuration.StorageConfiguration;
import poussecafe.journal.PollingPeriod;
import poussecafe.service.Workflow;
import poussecafe.storage.TransactionLessStorage;

public class TestApplicationConfiguration extends ApplicationConfiguration {

    private Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>> aggregateConfigurations;

    private Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>> processManagerConfigurations;

    private Set<Workflow> workflows;

    public TestApplicationConfiguration() {
        aggregateConfigurations = new HashSet<>();
        processManagerConfigurations = new HashSet<>();
        workflows = new HashSet<>();
    }

    public void registerAggregateConfiguration(ActiveStorableConfiguration<?, ?, ?, ?, ?> configuration) {
        aggregateConfigurations.add(configuration);
    }

    public void registerProcessManagerConfiguration(ActiveStorableConfiguration<?, ?, ?, ?, ?> configuration) {
        processManagerConfigurations.add(configuration);
    }

    public void registerWorkflow(Workflow service) {
        workflows.add(service);
    }

    @Override
    protected StorageConfiguration storageConfiguration() {
        return new TransactionLessStorage();
    }

    @Override
    protected ConsequenceJournalEntryConfiguration consequenceJournalEntryConfiguration() {
        return new InMemoryConsequenceJournalEntryConfiguration();
    }

    @Override
    protected Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>> aggregateConfigurations() {
        return aggregateConfigurations;
    }

    @Override
    protected Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>> processManagerConfigurations() {
        return processManagerConfigurations;
    }

    @Override
    protected Set<Workflow> workflows() {
        return workflows;
    }

    @Override
    public PollingPeriod getCommandWatcherPollingPeriod() {
        return PollingPeriod.withPeriod(Duration.ofMillis(10));
    }
}
