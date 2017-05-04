package poussecafe.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import poussecafe.journal.PollingPeriod;
import poussecafe.messaging.MessageSender;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.DefaultQueueSelector;
import poussecafe.messaging.InMemoryMessageQueue;
import poussecafe.messaging.Queue;
import poussecafe.messaging.QueueSelector;
import poussecafe.service.Workflow;
import poussecafe.storage.InMemoryStorage;
import poussecafe.storage.Storage;
import poussecafe.util.IdGenerator;

import static java.util.Arrays.asList;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class MetaApplicationConfiguration {

    private IdGenerator idGenerator;

    private QueueSelector sourceSelector;

    private MessagingJournalEntryConfiguration messagingJournalEntryConfiguration;

    @SuppressWarnings("rawtypes")
    private Set<ActiveStorableConfiguration> aggregateConfigurations;

    private Set<Workflow> workflows;

    private Set<Object> services;

    private Set<MessageSender> messageSenders;

    private Set<MessageReceiver> messageReceivers;

    private ProcessManagerConfiguration processManagerConfiguration;

    private List<Storage> storages;

    private Storage defaultStorage;

    public MetaApplicationConfiguration() {
        idGenerator = new IdGenerator();
        sourceSelector = new DefaultQueueSelector();
        messagingJournalEntryConfiguration = new MessagingJournalEntryConfiguration();
        aggregateConfigurations = new HashSet<>();
        workflows = new HashSet<>();
        services = new HashSet<>();

        Set<InMemoryMessageQueue> defaultMessageQueues = defaultMessageQueues();
        messageSenders = new HashSet<>(defaultMessageQueues);
        messageReceivers = new HashSet<>(defaultMessageQueues);

        processManagerConfiguration = new ProcessManagerConfiguration();

        storages = new ArrayList<>();
        defaultStorage = new InMemoryStorage();
    }

    protected IdGenerator idGenerator() {
        return new IdGenerator();
    }

    protected QueueSelector sourceSelectorSupplier() {
        return new DefaultQueueSelector();
    }

    private Set<InMemoryMessageQueue> defaultMessageQueues() {
        InMemoryMessageQueue commandQueue = new InMemoryMessageQueue(Queue.DEFAULT_COMMAND_QUEUE);
        InMemoryMessageQueue eventQueue = new InMemoryMessageQueue(Queue.DEFAULT_DOMAIN_EVENT_QUEUE);
        return new HashSet<>(asList(commandQueue, eventQueue));
    }

    @SuppressWarnings("rawtypes")
    public void registerAggregate(ActiveStorableConfiguration configuration) {
        aggregateConfigurations.add(configuration);
    }

    public void registerWorkflow(Workflow service) {
        workflows.add(service);
    }

    public void registerService(Object service) {
        services.add(service);
    }

    public void replaceEmitters(Set<MessageSender> emitters) {
        messageSenders.clear();
        messageSenders.addAll(emitters);
    }

    public void replaceReceivers(Set<MessageReceiver> receivers) {
        messageReceivers.clear();
        messageReceivers.addAll(receivers);
    }

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public QueueSelector getSourceSelector() {
        return sourceSelector;
    }

    public MessagingJournalEntryConfiguration getMessagingJournalEntryConfiguration() {
        return messagingJournalEntryConfiguration;
    }

    @SuppressWarnings("rawtypes")
    public Set<ActiveStorableConfiguration> getAggregateConfigurations() {
        return aggregateConfigurations;
    }

    public Set<Workflow> getWorkflows() {
        return workflows;
    }

    public Set<MessageSender> getMessageSenders() {
        return messageSenders;
    }

    public Set<MessageReceiver> getMessageReceivers() {
        return messageReceivers;
    }

    public PollingPeriod getCommandWatcherPollingPeriod() {
        return PollingPeriod.DEFAULT_POLLING_PERIOD;
    }

    public Set<Object> getServices() {
        return services;
    }

    public ProcessManagerConfiguration getProcessManagerConfiguration() {
        return processManagerConfiguration;
    }

    public void setStorages(List<Storage> storages) {
        this.storages.clear();
        this.storages.addAll(storages);
    }

    public List<Storage> getStorages() {
        return Collections.unmodifiableList(storages);
    }

    public void setDefaultStorage(Storage defaultStorage) {
        checkThat(value(defaultStorage).notNull().because("Default storage cannot be null"));
        this.defaultStorage = defaultStorage;
    }

    public Storage getDefaultStorage() {
        return defaultStorage;
    }
}
