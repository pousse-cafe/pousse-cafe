package poussecafe.journal.domain;

import java.util.List;
import poussecafe.domain.Service;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageAdapter;
import poussecafe.util.MapBuilder;

import static java.util.stream.Collectors.toList;

public class ConsumptionFailureRepository implements Service {

    public List<ConsumptionFailure> findConsumptionFailures(String messageId) {
        List<JournalEntry> entries = journalEntryRepository.findByMessageId(messageId);
        return map(entries);
    }

    private JournalEntryRepository journalEntryRepository;

    private List<ConsumptionFailure> map(List<JournalEntry> entries) {
        MapBuilder<Message, ConsumptionFailureBuilder> builders = new MapBuilder<>(this::newBuilder);
        for (JournalEntry entry : entries) {
            if (entry.getStatus() == JournalEntryStatus.FAILURE) {
                Message message = messageAdapter.adaptSerializedMessage(entry.getSerializedMessage());
                ConsumptionFailureBuilder builder = builders.getOrCreate(message);
                builder.addListener(entry.getKey().getListenerId());
            }
        }
        return builders.buildMap().values().stream().map(ConsumptionFailureBuilder::build).collect(toList());
    }

    private MessageAdapter messageAdapter;

    private ConsumptionFailureBuilder newBuilder(Message message) {
        return new ConsumptionFailureBuilder(message);
    }

    public List<ConsumptionFailure> findAllConsumptionFailures() {
        List<JournalEntry> entries = journalEntryRepository.findByStatus(JournalEntryStatus.FAILURE);
        return map(entries);
    }

}
