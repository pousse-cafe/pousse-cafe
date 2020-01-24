package poussecafe.environment;

import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.domain.DomainEvent;

import static poussecafe.util.ReferenceEquals.referenceEquals;

public class ExpectedEvent {

    public static class Builder {

        private ExpectedEvent producedEvent = new ExpectedEvent();

        public Builder producedEventClass(Class<? extends DomainEvent> producedEventClass) {
            producedEvent.producedEventClass = producedEventClass;
            return this;
        }

        public Builder required(boolean required) {
            producedEvent.required = required;
            return this;
        }

        public ExpectedEvent build() {
            Objects.requireNonNull(producedEvent.producedEventClass);
            return producedEvent;
        }
    }

    private ExpectedEvent() {

    }

    public Class<? extends DomainEvent> producedEventClass() {
        return producedEventClass;
    }

    private Class<? extends DomainEvent> producedEventClass;

    public boolean required() {
        return required;
    }

    private boolean required;

    public boolean matches(DomainEvent event) {
        return producedEventClass.isAssignableFrom(event.getClass());
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(producedEventClass, other.producedEventClass)
                .append(required, other.required)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(producedEventClass)
                .append(required)
                .build();
    }
}
