package poussecafe.journal.adapters;

import org.springframework.data.annotation.Id;
import poussecafe.journal.domain.ConsumptionStatus;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryKey;
import poussecafe.property.Property;
import poussecafe.property.PropertyBuilder;

public class MongoJournalEntryData implements JournalEntry.Data {

    @Override
    public Property<JournalEntryKey> key() {
        return PropertyBuilder.simple(JournalEntryKey.class)
                .fromAutoAdapting(SerializableJournalEntryKey.class)
                .get(() -> key)
                .set(value -> key = value)
                .build();
    }

    @Id
    private SerializableJournalEntryKey key;

    @Override
    public Property<String> rawMessage() {
        return PropertyBuilder.simple(String.class)
                .get(() -> rawMessage)
                .set(value -> rawMessage = value)
                .build();
    }

    private String rawMessage;

    @Override
    public Property<String> error() {
        return PropertyBuilder.simple(String.class)
                .get(() -> error)
                .set(value -> error = value)
                .build();
    }

    private String error;

    @Override
    public Property<ConsumptionStatus> status() {
        return PropertyBuilder.simple(ConsumptionStatus.class)
                .get(() -> status)
                .set(value -> status = value)
                .build();
    }

    private ConsumptionStatus status;
}
