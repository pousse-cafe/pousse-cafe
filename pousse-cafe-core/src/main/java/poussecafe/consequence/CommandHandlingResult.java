package poussecafe.consequence;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class CommandHandlingResult {

    private boolean success;

    private String failureDescription;

    private CommandHandlingResult(boolean success, String failureDescription) {
        setSuccess(success);
        setFailureDescription(failureDescription);
    }

    public static CommandHandlingResult success() {
        return new CommandHandlingResult(true, "");
    }

    public static CommandHandlingResult failure(String failureDescription) {
        return new CommandHandlingResult(false, failureDescription);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFailureDescription() {
        return failureDescription;
    }

    public void setFailureDescription(String failureDescription) {
        checkThat(value(failureDescription).notNull().because("Failure description cannot be null (but can be empty)"));
        this.failureDescription = failureDescription;
    }

    @Override
    public String toString() {
        return "CommandHandlingResult [success=" + success + ", failureDescription=" + failureDescription + "]";
    }
}
