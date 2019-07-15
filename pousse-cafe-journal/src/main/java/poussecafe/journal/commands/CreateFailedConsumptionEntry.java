package poussecafe.journal.commands;

import poussecafe.attribute.Attribute;
import poussecafe.journal.domain.JournalEntryId;
import poussecafe.runtime.Command;

public interface CreateFailedConsumptionEntry extends Command {

    Attribute<JournalEntryId> journalEntryId();

    Attribute<String> message();

    Attribute<String> error();
}
