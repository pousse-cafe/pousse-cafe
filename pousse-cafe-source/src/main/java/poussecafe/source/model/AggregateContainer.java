package poussecafe.source.model;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.generation.NamingConventions;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class AggregateContainer implements Serializable, WithTypeComponent {

    private TypeComponent typeComponent;

    @Override
    public TypeComponent typeComponent() {
        return typeComponent;
    }

    private Hooks hooks = Hooks.EMPTY;

    public Hooks hooks() {
        return hooks;
    }

    public String aggregateName() {
        return NamingConventions.aggregateNameFromContainer(typeComponent.typeName().rootClassName().simple());
    }

    public static class Builder {

        private AggregateContainer aggregate = new AggregateContainer();

        public AggregateContainer build() {
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

    private AggregateContainer() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(typeComponent, other.typeComponent)
                .append(hooks, other.hooks)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(typeComponent)
                .append(hooks)
                .build();
    }
}
