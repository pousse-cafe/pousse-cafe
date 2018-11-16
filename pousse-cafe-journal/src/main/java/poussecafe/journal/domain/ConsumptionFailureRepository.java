package poussecafe.journal.domain;

import java.util.List;
import poussecafe.domain.Service;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageAdapter;
import poussecafe.util.MapBuilder;

import static java.util.stream.Collectors.toList;

public class ConsumptionFailureRepository implements Service {

    public List<ConsumptionFailure> findConsumptionFailures(String consumptionId) {
        List<JournalEntry> entries = journalEntryRepository.findByConsumptionId(consumptionId);
        return map(entries);
    }

    private JournalEntryRepository journalEntryRepository;

    private List<ConsumptionFailure> map(List<JournalEntry> entries) {
        MapBuilder<ConsumptionFailureKey, ConsumptionFailureBuilder> builders = new MapBuilder<>(ConsumptionFailureBuilder::new);
        for (JournalEntry entry : entries) {
            if (entry.getStatus() == JournalEntryStatus.FAILURE) {
                Message message = messageAdapter.adaptSerializedMessage(entry.getSerializedMessage());
                String consumptionId = entry.getKey().getConsumptionId();
                ConsumptionFailureBuilder builder = builders.getOrCreate(new ConsumptionFailureKey(message, consumptionId));
                builder.addListener(entry.getKey().getListenerId());
            }
        }
        return builders.buildMap().values().stream().map(ConsumptionFailureBuilder::build).collect(toList());
    }

    private MessageAdapter messageAdapter;

    public List<ConsumptionFailure> findAllConsumptionFailures() {
        List<JournalEntry> entries = journalEntryRepository.findByStatus(JournalEntryStatus.FAILURE);
        return map(entries);
    }

}
