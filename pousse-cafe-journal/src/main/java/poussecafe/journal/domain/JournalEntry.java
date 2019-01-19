package poussecafe.journal.domain;

import poussecafe.context.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.Property;
import poussecafe.property.ProtectedProperty;
import poussecafe.property.ProtectedPropertyBuilder;

@Aggregate(
  factory = JournalEntryFactory.class,
  repository = JournalEntryRepository.class
)
public class JournalEntry extends AggregateRoot<JournalEntryKey, JournalEntry.Data> {

    public ProtectedProperty<ConsumptionStatus> status() {
        return ProtectedPropertyBuilder.protect(data().status())
                .of(this)
                .allowClassWrite(JournalEntryFactory.class)
                .build();
    }

    public ProtectedProperty<String> rawMessage() {
        return ProtectedPropertyBuilder.protect(data().rawMessage())
                .of(this)
                .allowClassWrite(JournalEntryFactory.class)
                .build();
    }

    public ProtectedProperty<String> error() {
        return ProtectedPropertyBuilder.protect(data().error())
                .of(this)
                .allowClassWrite(JournalEntryFactory.class)
                .build();
    }

    public static interface Data extends EntityData<JournalEntryKey> {

        Property<String> rawMessage();

        Property<String> error();

        Property<ConsumptionStatus> status();
    }
}
