package poussecafe.context;

import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.messaging.Message;
import poussecafe.util.ReferenceEquals;

public class RawAndAdaptedMessage {

    public static class Builder {

        private RawAndAdaptedMessage rawAndAdaptedMessage = new RawAndAdaptedMessage();

        public Builder raw(Object raw) {
            rawAndAdaptedMessage.raw = raw;
            return this;
        }

        public Builder adapted(Message adapted) {
            rawAndAdaptedMessage.adapted = adapted;
            return this;
        }

        public RawAndAdaptedMessage build() {
            Objects.requireNonNull(rawAndAdaptedMessage.raw);
            Objects.requireNonNull(rawAndAdaptedMessage.adapted);
            return rawAndAdaptedMessage;
        }
    }

    private RawAndAdaptedMessage() {

    }

    private Object raw;

    public Object raw() {
        return raw;
    }

    private Message adapted;

    public Message adapted() {
        return adapted;
    }

    @Override
    public boolean equals(Object obj) {
        return ReferenceEquals.referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(raw, other.raw)
                .append(adapted, other.adapted)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(raw)
                .append(adapted)
                .build();
    }
}
