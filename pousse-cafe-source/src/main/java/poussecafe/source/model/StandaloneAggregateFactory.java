package poussecafe.source.model;

import poussecafe.source.generation.NamingConventions;

import static java.util.Objects.requireNonNull;

public class StandaloneAggregateFactory {

    private TypeComponent typeComponent;

    public TypeComponent typeComponent() {
        return typeComponent;
    }

    public String aggregateName() {
        return NamingConventions.aggregateNameFromSimpleFactoryName(typeComponent.typeName().rootClassName().simple());
    }

    public static class Builder {

        private StandaloneAggregateFactory aggregate = new StandaloneAggregateFactory();

        public StandaloneAggregateFactory build() {
            requireNonNull(aggregate.typeComponent);
            return aggregate;
        }

        public Builder typeComponent(TypeComponent typeComponent) {
            aggregate.typeComponent = typeComponent;
            return this;
        }
    }

    private StandaloneAggregateFactory() {

    }
}
