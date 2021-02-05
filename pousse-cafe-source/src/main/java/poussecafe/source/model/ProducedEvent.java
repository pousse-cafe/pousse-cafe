package poussecafe.source.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import poussecafe.source.analysis.ProducedEventAnnotation;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class ProducedEvent implements Serializable {

    public Message message() {
        return message;
    }

    private Message message;

    public boolean required() {
        return required;
    }

    private boolean required;

    private List<String> consumedByExternal = emptyList();

    public List<String> consumedByExternal() {
        return Collections.unmodifiableList(consumedByExternal);
    }

    public static class Builder {

        private ProducedEvent event = new ProducedEvent();

        public ProducedEvent build() {
            requireNonNull(event.message);
            requireNonNull(event.consumedByExternal);
            return event;
        }

        public Builder message(Message message) {
            event.message = message;
            return this;
        }

        public Builder required(boolean required) {
            event.required = required;
            return this;
        }

        public Builder consumedByExternal(List<String> consumedByExternal) {
            event.consumedByExternal = consumedByExternal;
            return this;
        }

        public Builder withAnnotation(ProducedEventAnnotation annotation) {
            message(Message.ofTypeName(annotation.event()));
            required(annotation.required().orElse(true));
            consumedByExternal(annotation.consumedByExternal());
            return this;
        }
    }

    private ProducedEvent() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(message, other.message)
                .append(required, other.required)
                .append(consumedByExternal, other.consumedByExternal)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(message)
                .append(required)
                .append(consumedByExternal)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(message)
                .append(required)
                .append(consumedByExternal)
                .build();
    }
}
