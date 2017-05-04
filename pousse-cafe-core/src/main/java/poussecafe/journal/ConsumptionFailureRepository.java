package poussecafe.journal;

import java.util.List;
import poussecafe.messaging.Message;
import poussecafe.util.MapBuilder;

import static java.util.stream.Collectors.toList;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class ConsumptionFailureRepository {

    private JournalEntryRepository journalEntryRepository;

    public List<ConsumptionFailure> findConsumptionFailures(String messageId) {
        List<JournalEntry> entries = journalEntryRepository.findByMessageId(messageId);
        return map(entries);
    }

    private List<ConsumptionFailure> map(List<JournalEntry> entries) {
        MapBuilder<Message, ConsumptionFailureBuilder> builders = new MapBuilder<>(this::newBuilder);
        for (JournalEntry entry : entries) {
            if (entry.getStatus() == JournalEntryStatus.FAILURE) {
                ConsumptionFailureBuilder builder = builders.getOrCreate(entry.getMessage());
                builder.addListener(entry.getKey().getListenerId());
            }
        }
        return builders.buildMap().values().stream().map(ConsumptionFailureBuilder::build).collect(toList());
    }

    private ConsumptionFailureBuilder newBuilder(Message message) { // NOSONAR
        return new ConsumptionFailureBuilder(message);
    }

    public List<ConsumptionFailure> findAllConsumptionFailures() {
        List<JournalEntry> entries = journalEntryRepository.findByStatus(JournalEntryStatus.FAILURE);
        return map(entries);
    }

    public void setJournalEntryRepository(JournalEntryRepository journalEntryRepository) {
        checkThat(value(journalEntryRepository).notNull().because("Journal entry repository cannot be null"));
        this.journalEntryRepository = journalEntryRepository;
    }

}
