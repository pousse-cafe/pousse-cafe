package poussecafe.journal;

import poussecafe.environment.MessageListener;
import poussecafe.journal.commands.CreateFailedConsumptionEntry;
import poussecafe.journal.commands.CreateSuccessfulConsumptionEntry;
import poussecafe.journal.domain.JournalEntryId;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OriginalAndMarshaledMessage;
import poussecafe.runtime.Runtime;
import poussecafe.util.ExceptionUtils;

public class JournalMessageConsumptionHandler implements MessageConsumptionHandler {

    @Override
    public void handleSuccess(String consumptionId, OriginalAndMarshaledMessage receivedMessage, MessageListener listener) {
        CreateSuccessfulConsumptionEntry command = runtime.newCommand(CreateSuccessfulConsumptionEntry.class);
        command.journalEntryId().value(new JournalEntryId(consumptionId, listener.id()));
        command.message().value(marshaledMessageOrDefault(receivedMessage));
        runtime.submitCommand(command);
    }

    private String marshaledMessageOrDefault(OriginalAndMarshaledMessage receivedMessage) {
        if(receivedMessage.marshaled() instanceof String) {
            return (String) receivedMessage.marshaled();
        } else {
            return receivedMessage.original().toString();
        }
    }

    private Runtime runtime;

    @Override
    public void handleFailure(String consumptionId, OriginalAndMarshaledMessage receivedMessage, MessageListener listener, Exception e) {
        CreateFailedConsumptionEntry command = runtime.newCommand(CreateFailedConsumptionEntry.class);
        command.journalEntryId().value(new JournalEntryId(consumptionId, listener.id()));
        command.message().value(marshaledMessageOrDefault(receivedMessage));
        command.error().value(ExceptionUtils.getStackTrace(e));
        runtime.submitCommand(command);
    }
}
