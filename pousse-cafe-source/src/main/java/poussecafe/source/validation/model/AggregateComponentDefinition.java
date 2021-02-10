package poussecafe.source.validation.model;

import java.util.Optional;
import java.util.function.UnaryOperator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.generation.AggregatePackage;
import poussecafe.source.generation.NamingConventions;
import poussecafe.source.validation.SourceLine;
import poussecafe.source.validation.names.DeclaredComponent;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class AggregateComponentDefinition
implements DeclaredComponent, HasClassNameConvention {

    private SourceLine sourceLine;

    @Override
    public Optional<SourceLine> sourceLine() {
        return Optional.ofNullable(sourceLine);
    }

    @Override
    public ClassName className() {
        return className;
    }

    private ClassName className;

    public boolean isInnerClass() {
        return innerClass;
    }

    private boolean innerClass;

    @Override
    public boolean validClassName() {
        if(kind == AggregateComponentKind.FACTORY) {
            if(innerClass) {
                return NamingConventions.isInnerAggregateFactoryName(className.simple());
            } else {
                return NamingConventions.isStandaloneAggregateFactoryName(className.simple());
            }
        } else if(kind == AggregateComponentKind.ROOT) {
            if(innerClass) {
                return NamingConventions.isInnerAggregateRootName(className.simple());
            } else {
                return NamingConventions.isStandaloneAggregateRootName(className.simple());
            }
        } else if(kind == AggregateComponentKind.REPOSITORY) {
            if(innerClass) {
                return NamingConventions.isInnerAggregateRepositoryName(className.simple());
            } else {
                return NamingConventions.isStandaloneAggregateRepositoryName(className.simple());
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private AggregateComponentKind kind;

    public AggregatePackage aggregatePackage() {
        if(!validClassName()) {
            throw new IllegalStateException("Aggregate package cannot be computed properly with from invalid class name");
        }
        if(kind == AggregateComponentKind.FACTORY) {
            return aggregatePackage(NamingConventions::aggregateNameFromSimpleFactoryName);
        } else if(kind == AggregateComponentKind.ROOT) {
            return aggregatePackage(NamingConventions::aggregateNameFromSimpleRootName);
        } else if(kind == AggregateComponentKind.REPOSITORY) {
            return aggregatePackage(NamingConventions::aggregateNameFromSimpleRepositoryName);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private AggregatePackage aggregatePackage(UnaryOperator<String> aggregateNameFunction) {
        String[] classNameSegments = className.segments();
        if(innerClass) {
            return new AggregatePackage(className.withoutLastSegments(2).qualified(),
                    NamingConventions.aggregateNameFromContainer(className.segments()[classNameSegments.length - 2]));
        } else {
            return new AggregatePackage(className.withoutLastSegment().qualified(),
                    aggregateNameFunction.apply(className.segments()[classNameSegments.length - 1]));
        }
    }

    public static class Builder {

        public AggregateComponentDefinition build() {
            requireNonNull(definition.className);
            requireNonNull(definition.kind);
            return definition;
        }

        private AggregateComponentDefinition definition = new AggregateComponentDefinition();

        public Builder sourceLine(SourceLine sourceLine) {
            definition.sourceLine = sourceLine;
            return this;
        }

        public Builder className(ClassName className) {
            definition.className = className;
            return this;
        }

        public Builder innerClass(boolean innerClass) {
            definition.innerClass = innerClass;
            return this;
        }

        public Builder kind(AggregateComponentKind kind) {
            definition.kind = kind;
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
                .append(kind, other.kind)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(className)
                .append(innerClass)
                .append(sourceLine)
                .append(kind)
                .build();
    }
}
