package poussecafe.source.model;

import poussecafe.source.generation.NamingConventions;

import static java.util.Objects.requireNonNull;

public class StandaloneAggregateRepository {

    private TypeComponent typeComponent;

    public TypeComponent typeComponent() {
        return typeComponent;
    }

    public String aggregateName() {
        return NamingConventions.aggregateNameFromSimpleRepositoryName(typeComponent.typeName().rootClassName().simple());
    }

    public static class Builder {

        private StandaloneAggregateRepository aggregate = new StandaloneAggregateRepository();

        public StandaloneAggregateRepository build() {
            requireNonNull(aggregate.typeComponent);
            return aggregate;
        }

        public Builder typeComponent(TypeComponent typeComponent) {
            aggregate.typeComponent = typeComponent;
            return this;
        }
    }

    private StandaloneAggregateRepository() {

    }
}
