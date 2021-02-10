package poussecafe.source.validation.model;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.generation.AggregatePackage;
import poussecafe.source.generation.NamingConventions;
import poussecafe.source.model.TypeComponent;
import poussecafe.source.validation.SourceLine;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class AggregateContainer implements Serializable {

    private TypeComponent typeComponent;

    public TypeComponent typeComponent() {
        return typeComponent;
    }

    public AggregatePackage aggregatePackage() {
        var containerClassName = typeComponent.typeName().rootClassName();
        var aggregateName = NamingConventions.aggregateNameFromContainer(containerClassName.simple());
        return new AggregatePackage(containerClassName.qualifier(), aggregateName);
    }

    public SourceLine sourceLine() {
        return sourceLine;
    }

    private SourceLine sourceLine;

    public static class Builder {

        private AggregateContainer aggregate = new AggregateContainer();

        public AggregateContainer build() {
            requireNonNull(aggregate.typeComponent);
            requireNonNull(aggregate.sourceLine);
            return aggregate;
        }

        public Builder typeComponent(TypeComponent typeComponent) {
            aggregate.typeComponent = typeComponent;
            return this;
        }

        public Builder sourceLine(SourceLine sourceLine) {
            aggregate.sourceLine = sourceLine;
            return this;
        }
    }

    private AggregateContainer() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(typeComponent, other.typeComponent)
                .append(sourceLine, other.sourceLine)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(typeComponent)
                .append(sourceLine)
                .build();
    }
}
