package poussecafe.journal.domain;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.Factory;
import poussecafe.journal.commands.CreateFailedConsumptionEntry;
import poussecafe.journal.commands.CreateSuccessfulConsumptionEntry;

public class JournalEntryFactory extends Factory<JournalEntryId, JournalEntry, JournalEntry.Attributes> {

    @MessageListener
    public JournalEntry buildSuccessEntry(CreateSuccessfulConsumptionEntry command) {
        JournalEntry entry = newAggregateWithId(command.journalEntryId().value());
        entry.attributes().status().value(ConsumptionStatus.SUCCESS);
        entry.attributes().rawMessage().valueOf(command.message());
        return entry;
    }

    @MessageListener
    public JournalEntry buildFailureEntry(CreateFailedConsumptionEntry command) {
        JournalEntry entry = newAggregateWithId(command.journalEntryId().value());
        entry.attributes().status().value(ConsumptionStatus.FAILURE);
        entry.attributes().rawMessage().valueOf(command.message());
        entry.attributes().error().valueOf(command.error());
        return entry;
    }
}
