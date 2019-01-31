package poussecafe.journal.adapters;

import java.util.List;
import poussecafe.contextconfigurer.DataAccessImplementation;
import poussecafe.journal.domain.ConsumptionStatus;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryKey;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.Arrays.asList;

@DataAccessImplementation(
    aggregateRoot = JournalEntry.class,
    dataImplementation = JournalEntryData.class,
    storageName = InternalStorage.NAME
)
public class InternalJournalEntryDataAccess extends InternalDataAccess<JournalEntryKey, JournalEntryData> implements poussecafe.journal.domain.JournalEntryDataAccess<JournalEntryData> {

    @Override
    protected List<Object> extractIndexedData(JournalEntryData data) {
        return asList(data.key().get().getConsumptionId(), data.status().get());
    }

    @Override
    public List<JournalEntryData> findByMessageId(String messageId) {
        return findBy(messageId);
    }

    @Override
    public List<JournalEntryData> findByStatus(ConsumptionStatus status) {
        return findBy(status);
    }

}
