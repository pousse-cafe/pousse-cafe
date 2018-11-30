package poussecafe.journal.domain;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.Property;
import poussecafe.property.ProtectedProperty;
import poussecafe.property.ProtectedPropertyBuilder;

public class JournalEntry extends AggregateRoot<JournalEntryKey, JournalEntry.Data> {

    public ProtectedProperty<ConsumptionStatus> status() {
        return ProtectedPropertyBuilder.protect(getData().status())
                .of(this)
                .allowClassWrite(JournalEntryFactory.class)
                .build();
    }

    public ProtectedProperty<String> rawMessage() {
        return ProtectedPropertyBuilder.protect(getData().rawMessage())
                .of(this)
                .allowClassWrite(JournalEntryFactory.class)
                .build();
    }

    public ProtectedProperty<String> error() {
        return ProtectedPropertyBuilder.protect(getData().error())
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
