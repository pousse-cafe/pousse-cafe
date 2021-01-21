package poussecafe.source.model;

import java.util.Optional;
import poussecafe.source.Source;

import static java.util.Objects.requireNonNull;

public class DomainEvent extends ComponentWithType {

    private Optional<Source> source = Optional.empty();

    public Optional<Source> source() {
        return source;
    }

    public static class Builder {

        private DomainEvent event = new DomainEvent();

        public DomainEvent build() {
            requireNonNull(event.name);
            requireNonNull(event.packageName);
            requireNonNull(event.source);
            return event;
        }

        public Builder source(Optional<Source> source) {
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
}
