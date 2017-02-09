package poussecafe.sample.workflow;

import poussecafe.process.ErrorState;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.emptyOrNullString;
import static poussecafe.check.Predicates.not;

public class OrderPlacementError extends ErrorState {

    private String errorDescription;

    public OrderPlacementError(String description) {
        setErrorDescription(description);
    }

    private void setErrorDescription(String errorDescription) {
        checkThat(value(errorDescription)
                .verifies(not(emptyOrNullString()))
                .because("Error description cannot be empty"));
        this.errorDescription = errorDescription;
    }

    @Override
    public String getErrorDescription() {
        return errorDescription;
    }
}