package poussecafe.journal.domain;

import poussecafe.domain.Factory;
import poussecafe.support.model.FailedConsumption;
import poussecafe.support.model.SuccessfulConsumption;

public class JournalEntryFactory extends Factory<JournalEntryId, JournalEntry, JournalEntry.Attributes> {

    public JournalEntry buildEntry(SuccessfulConsumption event) {
        JournalEntry entry = newAggregateWithId(new JournalEntryId(event.consumptionId().value(), event.listenerId().value()));
        entry.attributes().rawMessage().value(event.rawMessage().value());
        entry.attributes().status().value(ConsumptionStatus.SUCCESS);
        return entry;
    }

    public JournalEntry buildEntry(FailedConsumption event) {
        JournalEntry entry = newAggregateWithId(new JournalEntryId(event.consumptionId().value(), event.listenerId().value()));
        entry.attributes().rawMessage().value(event.rawMessage().value());
        entry.attributes().status().value(ConsumptionStatus.FAILURE);
        entry.attributes().error().value(event.error().value());
        return entry;
    }
}
