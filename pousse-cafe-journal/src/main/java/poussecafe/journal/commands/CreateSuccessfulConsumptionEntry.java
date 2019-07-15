package poussecafe.journal.commands;

import poussecafe.attribute.Attribute;
import poussecafe.journal.domain.JournalEntryId;
import poussecafe.runtime.Command;

public interface CreateSuccessfulConsumptionEntry extends Command {

    Attribute<JournalEntryId> journalEntryId();

    Attribute<String> message();
}
