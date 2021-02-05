package poussecafe.source.validation.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.validation.SourceLine;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class EntityImplementation implements Serializable {

    private SourceLine sourceFileLine;

    public SourceLine sourceFileLine() {
        return sourceFileLine;
    }

    public Optional<String> entityImplementationQualifiedClassName() {
        return Optional.ofNullable(entityImplementationQualifiedClassName);
    }

    private String entityImplementationQualifiedClassName;

    public Optional<String> entityDefinitionQualifiedClassName() {
        return Optional.ofNullable(entityDefinitionQualifiedClassName);
    }

    private String entityDefinitionQualifiedClassName;

    public List<String> storageNames() {
        return Collections.unmodifiableList(storageNames);
    }

    private List<String> storageNames;

    public static class Builder {

        public EntityImplementation build() {
            requireNonNull(implementation.sourceFileLine);
            return implementation;
        }

        private EntityImplementation implementation = new EntityImplementation();

        public Builder sourceFileLine(SourceLine sourceFileLine) {
            implementation.sourceFileLine = sourceFileLine;
            return this;
        }

        public Builder entityImplementationQualifiedClassName(Optional<String> entityImplementationQualifiedClassName) {
            implementation.entityImplementationQualifiedClassName = entityImplementationQualifiedClassName.orElse(null);
            return this;
        }

        public Builder entityDefinitionQualifiedClassName(Optional<String> entityDefinitionQualifiedClassName) {
            implementation.entityDefinitionQualifiedClassName = entityDefinitionQualifiedClassName.orElse(null);
            return this;
        }

        public Builder storageNames(List<String> storageNames) {
            implementation.storageNames = storageNames;
            return this;
        }
    }

    private EntityImplementation() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(entityDefinitionQualifiedClassName, other.entityDefinitionQualifiedClassName)
                .append(entityImplementationQualifiedClassName, other.entityImplementationQualifiedClassName)
                .append(sourceFileLine, other.sourceFileLine)
                .append(storageNames, other.storageNames)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(entityDefinitionQualifiedClassName)
                .append(entityImplementationQualifiedClassName)
                .append(sourceFileLine)
                .append(storageNames)
                .build();
    }
}
