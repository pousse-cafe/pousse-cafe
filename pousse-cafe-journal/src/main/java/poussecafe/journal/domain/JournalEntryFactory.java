package poussecafe.journal.domain;

import poussecafe.domain.Factory;
import poussecafe.events.FailedConsumption;
import poussecafe.events.SuccessfulConsumption;

public class JournalEntryFactory extends Factory<JournalEntryKey, JournalEntry, JournalEntry.Data> {

    public JournalEntry buildEntry(SuccessfulConsumption event) {
        JournalEntry entry = newAggregateWithKey(new JournalEntryKey(event.consumptionId().get(), event.listenerId().get()));
        entry.rawMessage().let(this).set(event.rawMessage().get());
        entry.status().let(this).set(ConsumptionStatus.SUCCESS);
        return entry;
    }

    public JournalEntry buildEntry(FailedConsumption event) {
        JournalEntry entry = newAggregateWithKey(new JournalEntryKey(event.consumptionId().get(), event.listenerId().get()));
        entry.rawMessage().let(this).set(event.rawMessage().get());
        entry.status().let(this).set(ConsumptionStatus.FAILURE);
        entry.error().let(this).set(event.error().get());
        return entry;
    }
}
