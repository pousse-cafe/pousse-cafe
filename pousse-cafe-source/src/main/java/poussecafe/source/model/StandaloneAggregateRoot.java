package poussecafe.source.model;

import poussecafe.source.generation.NamingConventions;

import static java.util.Objects.requireNonNull;

public class StandaloneAggregateRoot {

    private TypeComponent typeComponent;

    public TypeComponent typeComponent() {
        return typeComponent;
    }

    private Hooks hooks;

    public Hooks hooks() {
        return hooks;
    }

    public String aggregateName() {
        return NamingConventions.aggregateNameFromSimpleRootName(typeComponent.typeName().rootClassName().simple());
    }

    public static class Builder {

        private StandaloneAggregateRoot aggregate = new StandaloneAggregateRoot();

        public StandaloneAggregateRoot build() {
            requireNonNull(aggregate.typeComponent);
            requireNonNull(aggregate.hooks);
            return aggregate;
        }

        public Builder typeComponent(TypeComponent typeComponent) {
            aggregate.typeComponent = typeComponent;
            return this;
        }

        public Builder hooks(Hooks hooks) {
            aggregate.hooks = hooks;
            return this;
        }
    }

    private StandaloneAggregateRoot() {

    }
}