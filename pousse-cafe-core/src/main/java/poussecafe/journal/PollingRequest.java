package poussecafe.journal;

import java.util.concurrent.CompletableFuture;
import poussecafe.consequence.CommandHandlingResult;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.emptyOrNullString;
import static poussecafe.check.Predicates.not;

public class PollingRequest {

    private String consequenceId;

    private CompletableFuture<CommandHandlingResult> completable;

    public PollingRequest(String consequenceId, CompletableFuture<CommandHandlingResult> completable) {
        setConsequenceId(consequenceId);
        setFuture(completable);
    }

    public String getConsequenceId() {
        return consequenceId;
    }

    private void setConsequenceId(String consequenceId) {
        checkThat(value(consequenceId).verifies(not(emptyOrNullString())).because("Consequence ID cannot be empty"));
        this.consequenceId = consequenceId;
    }

    public CompletableFuture<CommandHandlingResult> getFuture() {
        return completable;
    }

    private void setFuture(CompletableFuture<CommandHandlingResult> future) {
        checkThat(value(future).notNull().because("Future cannot be empty"));
        completable = future;
    }

    public void completeWithSuccess() {
        completable.complete(CommandHandlingResult.success());
    }

    public void completeWithFailure(String description) {
        completable.complete(CommandHandlingResult.failure(description));
    }
}
