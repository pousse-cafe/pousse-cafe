package poussecafe.journal;

import poussecafe.journal.commands.CreateFailedConsumptionEntry;
import poussecafe.journal.commands.CreateSuccessfulConsumptionEntry;
import poussecafe.journal.domain.JournalEntryId;

public abstract class MessagingJournalTest extends JournalTest {

    protected String listenerId;

    protected String message;

    protected String exception;

    protected JournalEntryId id;

    protected void givenSuccessfullyConsumedMessage() {
        givenReceivedMessage();
    }

    protected void givenReceivedMessage() {
        listenerId = "listenerId";
        message = "message";
        givenId();
    }

    protected void givenId() {
        id = new JournalEntryId("consumptionId", listenerId);
    }

    protected void givenIgnoredMessage() {
        givenReceivedMessage();
    }

    protected void givenMessageConsumptionFailed() {
        givenReceivedMessage();
        exception = "error";
    }

    protected void whenLoggingSuccessfulConsumption() {
        CreateSuccessfulConsumptionEntry command = newCommand(CreateSuccessfulConsumptionEntry.class);
        command.journalEntryId().value(id);
        command.message().value(message);
        submitCommand(command);
    }

    protected void whenLoggingFailedConsumption() {
        CreateFailedConsumptionEntry command = newCommand(CreateFailedConsumptionEntry.class);
        command.journalEntryId().value(id);
        command.message().value(message);
        command.error().value("error");
        submitCommand(command);
    }
}
