package poussecafe.source.validation.model;

import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.analysis.Name;
import poussecafe.source.validation.SourceLine;
import poussecafe.source.validation.names.DeclaredComponent;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class AggregateComponentDefinition implements DeclaredComponent {

    private SourceLine sourceLine;

    @Override
    public Optional<SourceLine> sourceLine() {
        return Optional.ofNullable(sourceLine);
    }

    @Override
    public Name className() {
        return className;
    }

    private Name className;

    public boolean isInnerClass() {
        return innerClass;
    }

    private boolean innerClass;

    public static class Builder {

        public AggregateComponentDefinition build() {
            requireNonNull(definition.sourceLine);
            requireNonNull(definition.className);
            return definition;
        }

        private AggregateComponentDefinition definition = new AggregateComponentDefinition();

        public Builder sourceLine(SourceLine sourceLine) {
            definition.sourceLine = sourceLine;
            return this;
        }

        public Builder className(Name className) {
            definition.className = className;
            return this;
        }

        public Builder innerClass(boolean innerClass) {
            definition.innerClass = innerClass;
            return this;
        }
    }

    private AggregateComponentDefinition() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(className, other.className)
                .append(innerClass, other.innerClass)
                .append(sourceLine, other.sourceLine)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(className)
                .append(innerClass)
                .append(sourceLine)
                .build();
    }
}
