package poussecafe.source.validation;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class ValidationResult {

    public ValidationResult(List<ValidationMessage> messages) {
        requireNonNull(messages);
        this.messages = messages;
    }

    private List<ValidationMessage> messages;

    public List<ValidationMessage> messages() {
        return messages;
    }
}
