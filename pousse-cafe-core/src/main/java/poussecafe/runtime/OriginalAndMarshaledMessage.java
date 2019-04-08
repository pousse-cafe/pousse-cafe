package poussecafe.runtime;

import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.messaging.Message;
import poussecafe.util.ReferenceEquals;

public class OriginalAndMarshaledMessage {

    public static class Builder {

        private OriginalAndMarshaledMessage rawAndAdaptedMessage = new OriginalAndMarshaledMessage();

        public Builder marshaled(Object marshaled) {
            rawAndAdaptedMessage.marshaled = marshaled;
            return this;
        }

        public Builder original(Message original) {
            rawAndAdaptedMessage.original = original;
            return this;
        }

        public OriginalAndMarshaledMessage build() {
            Objects.requireNonNull(rawAndAdaptedMessage.original);
            Objects.requireNonNull(rawAndAdaptedMessage.original);
            return rawAndAdaptedMessage;
        }
    }

    private OriginalAndMarshaledMessage() {

    }

    private Object marshaled;

    public Object marshaled() {
        return marshaled;
    }

    private Message original;

    public Message original() {
        return original;
    }

    @Override
    public boolean equals(Object obj) {
        return ReferenceEquals.referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(original, other.original)
                .append(marshaled, other.marshaled)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(original)
                .append(marshaled)
                .build();
    }
}
