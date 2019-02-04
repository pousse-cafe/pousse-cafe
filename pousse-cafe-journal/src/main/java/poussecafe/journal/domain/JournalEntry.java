package poussecafe.journal.domain;

import poussecafe.contextconfigurer.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.Property;

@Aggregate(
  factory = JournalEntryFactory.class,
  repository = JournalEntryRepository.class
)
public class JournalEntry extends AggregateRoot<JournalEntryKey, JournalEntry.Data> {

    public static interface Data extends EntityData<JournalEntryKey> {

        Property<String> rawMessage();

        Property<String> error();

        Property<ConsumptionStatus> status();
    }
}
