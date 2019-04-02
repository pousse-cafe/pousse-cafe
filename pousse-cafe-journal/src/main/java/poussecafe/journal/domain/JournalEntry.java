package poussecafe.journal.domain;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = JournalEntryFactory.class,
  repository = JournalEntryRepository.class
)
public class JournalEntry extends AggregateRoot<JournalEntryId, JournalEntry.Attributes> {

    public static interface Attributes extends EntityAttributes<JournalEntryId> {

        Attribute<String> rawMessage();

        Attribute<String> error();

        Attribute<ConsumptionStatus> status();
    }
}
