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

    public boolean hasError() {
        return hasType(ValidationMessageType.ERROR);
    }

    private boolean hasType(ValidationMessageType type) {
        return messages.stream().anyMatch(message -> message.type() == type);
    }

    public boolean hasWarning() {
        return hasType(ValidationMessageType.WARNING);
    }
}
