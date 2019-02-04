package poussecafe.journal.domain;

import poussecafe.domain.Factory;
import poussecafe.support.model.FailedConsumption;
import poussecafe.support.model.SuccessfulConsumption;

public class JournalEntryFactory extends Factory<JournalEntryKey, JournalEntry, JournalEntry.Data> {

    public JournalEntry buildEntry(SuccessfulConsumption event) {
        JournalEntry entry = newAggregateWithKey(new JournalEntryKey(event.consumptionId().get(), event.listenerId().get()));
        entry.data().rawMessage().set(event.rawMessage().get());
        entry.data().status().set(ConsumptionStatus.SUCCESS);
        return entry;
    }

    public JournalEntry buildEntry(FailedConsumption event) {
        JournalEntry entry = newAggregateWithKey(new JournalEntryKey(event.consumptionId().get(), event.listenerId().get()));
        entry.data().rawMessage().set(event.rawMessage().get());
        entry.data().status().set(ConsumptionStatus.FAILURE);
        entry.data().error().set(event.error().get());
        return entry;
    }
}
