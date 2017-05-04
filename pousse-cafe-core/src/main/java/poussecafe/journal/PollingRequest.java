package poussecafe.journal;

import java.util.concurrent.CompletableFuture;
import poussecafe.messaging.CommandHandlingResult;
import poussecafe.process.ProcessManagerKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.emptyOrNullString;
import static poussecafe.check.Predicates.not;

public class PollingRequest {

    private String commandId;

    private CompletableFuture<CommandHandlingResult> completable;

    private ProcessManagerKey processManagerKey;

    public PollingRequest(String commandId, CompletableFuture<CommandHandlingResult> completable) {
        setCommandId(commandId);
        setFuture(completable);
    }

    public String getCommandId() {
        return commandId;
    }

    private void setCommandId(String commandId) {
        checkThat(value(commandId).verifies(not(emptyOrNullString())).because("Command ID cannot be empty"));
        this.commandId = commandId;
    }

    public CompletableFuture<CommandHandlingResult> getFuture() {
        return completable;
    }

    private void setFuture(CompletableFuture<CommandHandlingResult> future) {
        checkThat(value(future).notNull().because("Future cannot be empty"));
        completable = future;
    }

    public ProcessManagerKey getProcessManagerKey() {
        return processManagerKey;
    }

    public void setProcessManagerKey(ProcessManagerKey processManagerKey) {
        this.processManagerKey = processManagerKey;
    }

    public void completeWithSuccess() {
        completable.complete(CommandHandlingResult.success());
    }

    public void completeWithFailure(String description) {
        completable.complete(CommandHandlingResult.failure(description));
    }

    public boolean hasProcessManagerKey() {
        return processManagerKey != null;
    }

}
