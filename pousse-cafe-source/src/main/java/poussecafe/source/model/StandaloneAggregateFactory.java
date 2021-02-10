package poussecafe.source.model;

import java.io.Serializable;
import poussecafe.source.generation.NamingConventions;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("serial")
public class StandaloneAggregateFactory implements Serializable, WithTypeComponent {

    private TypeComponent typeComponent;

    @Override
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
