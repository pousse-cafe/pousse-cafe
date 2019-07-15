package poussecafe.journal.commands.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.journal.commands.CreateSuccessfulConsumptionEntry;
import poussecafe.journal.domain.JournalEntryId;

@MessageImplementation(message = CreateSuccessfulConsumptionEntry.class)
@SuppressWarnings("serial")
public class CreateSuccessfulConsumptionEntryData implements Serializable, CreateSuccessfulConsumptionEntry {

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
}
