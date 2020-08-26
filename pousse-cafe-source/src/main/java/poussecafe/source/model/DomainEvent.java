package poussecafe.source.model;

import static java.util.Objects.requireNonNull;

public class DomainEvent extends ComponentWithType {

    public static class Builder {

        private DomainEvent source = new DomainEvent();

        public DomainEvent build() {
            requireNonNull(source.simpleName);
            requireNonNull(source.packageName);
            return source;
        }

        public Builder name(String name) {
            source.simpleName = name;
            return this;
        }

        public Builder packageName(String packageName) {
            source.packageName = packageName;
            return this;
        }
    }

    private DomainEvent() {

    }
}
