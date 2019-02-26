package poussecafe.journal.domain;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = JournalEntryFactory.class,
  repository = JournalEntryRepository.class
)
public class JournalEntry extends AggregateRoot<JournalEntryKey, JournalEntry.Attributes> {

    public static interface Attributes extends EntityAttributes<JournalEntryKey> {

        Attribute<String> rawMessage();

        Attribute<String> error();

        Attribute<ConsumptionStatus> status();
    }
}
