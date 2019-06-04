package poussecafe.journal.adapters;

import java.util.List;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.journal.domain.ConsumptionStatus;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryId;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.Arrays.asList;

@DataAccessImplementation(
    aggregateRoot = JournalEntry.class,
    dataImplementation = JournalEntryData.class,
    storageName = InternalStorage.NAME
)
public class InternalJournalEntryDataAccess extends InternalDataAccess<JournalEntryId, JournalEntryData> implements poussecafe.journal.domain.JournalEntryDataAccess<JournalEntryData> {

    @Override
    protected List<Object> extractIndexedData(JournalEntryData data) {
        return asList(data.identifier().value().getConsumptionId(), data.status().value());
    }

    @Override
    public List<JournalEntryData> findByStatus(ConsumptionStatus status) {
        return findBy(status);
    }

}
