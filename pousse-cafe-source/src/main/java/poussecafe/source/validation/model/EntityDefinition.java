package poussecafe.source.validation.model;

import java.io.Serializable;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.analysis.Name;
import poussecafe.source.validation.SourceLine;
import poussecafe.source.validation.names.NamedComponent;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class EntityDefinition implements NamedComponent, Serializable {

    private String entityName;

    @Override
    public String name() {
        return entityName;
    }

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

    public static class Builder {

        public EntityDefinition build() {
            requireNonNull(definition.entityName);
            requireNonNull(definition.sourceLine);
            requireNonNull(definition.className);
            return definition;
        }

        private EntityDefinition definition = new EntityDefinition();

        public Builder entityName(String entityName) {
            definition.entityName = entityName;
            return this;
        }

        public Builder sourceLine(SourceLine sourceLine) {
            definition.sourceLine = sourceLine;
            return this;
        }

        public Builder className(Name className) {
            definition.className = className;
            return this;
        }
    }

    private EntityDefinition() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(className, other.className)
                .append(entityName, other.entityName)
                .append(sourceLine, other.sourceLine)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(className)
                .append(entityName)
                .append(sourceLine)
                .build();
    }
}
