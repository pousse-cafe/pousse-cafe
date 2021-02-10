package poussecafe.source.model;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.Source;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class DomainEvent extends ComponentWithType implements Serializable {

    public static class Builder {

        private DomainEvent event = new DomainEvent();

        public DomainEvent build() {
            requireNonNull(event.name);
            requireNonNull(event.packageName);
            requireNonNull(event.source);
            return event;
        }

        public Builder source(Source source) {
            event.source = source;
            return this;
        }

        public Builder name(String name) {
            event.name = name;
            return this;
        }

        public Builder packageName(String packageName) {
            event.packageName = packageName;
            return this;
        }
    }

    private DomainEvent() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(source, other.source)
                .append(name, other.name)
                .append(packageName, other.packageName)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(source)
                .append(name)
                .append(packageName)
                .build();
    }
}
