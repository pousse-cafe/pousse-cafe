package poussecafe.journal.adapters;

import org.springframework.data.annotation.Id;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.journal.domain.ConsumptionStatus;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryKey;

public class MongoJournalEntryData implements JournalEntry.Attributes {

    @Override
    public Attribute<JournalEntryKey> key() {
        return AttributeBuilder.simple(JournalEntryKey.class)
                .fromAutoAdapting(SerializableJournalEntryKey.class)
                .get(() -> key)
                .set(value -> key = value)
                .build();
    }

    @Id
    private SerializableJournalEntryKey key;

    @Override
    public Attribute<String> rawMessage() {
        return AttributeBuilder.simple(String.class)
                .get(() -> rawMessage)
                .set(value -> rawMessage = value)
                .build();
    }

    private String rawMessage;

    @Override
    public Attribute<String> error() {
        return AttributeBuilder.simple(String.class)
                .get(() -> error)
                .set(value -> error = value)
                .build();
    }

    private String error;

    @Override
    public Attribute<ConsumptionStatus> status() {
        return AttributeBuilder.simple(ConsumptionStatus.class)
                .get(() -> status)
                .set(value -> status = value)
                .build();
    }

    private ConsumptionStatus status;
}
