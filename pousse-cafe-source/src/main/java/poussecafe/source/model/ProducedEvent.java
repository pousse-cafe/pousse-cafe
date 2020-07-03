package poussecafe.source.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

public class ProducedEvent {

    public String eventName() {
        return eventName;
    }

    private String eventName;

    public boolean required() {
        return required;
    }

    private boolean required;

    public static class Builder {

        private ProducedEvent event = new ProducedEvent();

        public ProducedEvent build() {
            requireNonNull(event.eventName);
            return event;
        }

        public Builder eventName(String eventName) {
            event.eventName = eventName;
            return this;
        }

        public Builder required(boolean required) {
            event.required = required;
            return this;
        }
    }

    private ProducedEvent() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(eventName, other.eventName)
                .append(required, other.required)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(eventName)
                .append(required)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(eventName)
                .append(required)
                .build();
    }
}
