package poussecafe.journal.commands.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.journal.commands.CreateFailedConsumptionEntry;
import poussecafe.journal.domain.JournalEntryId;

@MessageImplementation(message = CreateFailedConsumptionEntry.class)
@SuppressWarnings("serial")
public class CreateFailedConsumptionEntryData implements Serializable, CreateFailedConsumptionEntry {

    @Override
    public Attribute<JournalEntryId> journalEntryId() {
        return AttributeBuilder.single(JournalEntryId.class)
                .usingAutoAdapter(JournalEntryIdData.class)
                .read(() -> journalEntryId)
                .write(value -> journalEntryId = value)
                .build();
    }

    private JournalEntryIdData journalEntryId;

    @Override
    public Attribute<String> message() {
        return AttributeBuilder.single(String.class)
                .read(() -> message)
                .write(value -> message = value)
                .build();
    }

    private String message;

    @Override
    public Attribute<String> error() {
        return AttributeBuilder.single(String.class)
                .read(() -> error)
                .write(value -> error = value)
                .build();
    }

    private String error;
}
