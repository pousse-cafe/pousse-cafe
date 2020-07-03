package poussecafe.source.model;

import java.util.Optional;
import poussecafe.source.analysis.ResolvedTypeName;

import static java.util.Objects.requireNonNull;

public class Aggregate {

    private String name;

    public String name() {
        return name;
    }

    public static class Builder {

        private Aggregate aggregate = new Aggregate();

        public Aggregate build() {
            requireNonNull(aggregate.name);
            return aggregate;
        }

        public Builder name(ResolvedTypeName name) {
            aggregate.name = name.simpleName();
            return this;
        }

        public Optional<String> name() {
            return Optional.ofNullable(aggregate.name);
        }

        public Builder startingFrom(Aggregate other) {
            aggregate.name = other.name;
            return this;
        }
    }

    private Aggregate() {

    }
}
