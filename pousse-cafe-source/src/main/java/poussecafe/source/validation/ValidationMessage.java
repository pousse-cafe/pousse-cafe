package poussecafe.source.validation;

import static java.util.Objects.requireNonNull;

public class ValidationMessage {

    public String message() {
        return message;
    }

    private String message;

    public ValidationMessageType type() {
        return type;
    }

    private ValidationMessageType type;

    public SourceLine location() {
        return location;
    }

    private SourceLine location;

    public static class Builder {

        public ValidationMessage build() {
            requireNonNull(message.message);
            requireNonNull(message.type);
            requireNonNull(message.location);
            return message;
        }

        private ValidationMessage message = new ValidationMessage();

        public Builder message(String message) {
            this.message.message = message;
            return this;
        }

        public Builder type(ValidationMessageType type) {
            message.type = type;
            return this;
        }

        public Builder location(SourceLine location) {
            message.location = location;
            return this;
        }
    }

    private ValidationMessage() {

    }
}
