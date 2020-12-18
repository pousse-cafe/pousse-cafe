package poussecafe.source.validation.model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import poussecafe.source.validation.SourceFileLine;

import static java.util.Objects.requireNonNull;

public class EntityImplementation {

    private SourceFileLine sourceFileLine;

    public SourceFileLine sourceFileLine() {
        return sourceFileLine;
    }

    public Optional<String> entityDefinitionQualifiedClassName() {
        return entityDefinitionQualifiedClassName;
    }

    private Optional<String> entityDefinitionQualifiedClassName = Optional.empty();

    public List<String> storageNames() {
        return Collections.unmodifiableList(storageNames);
    }

    private List<String> storageNames;

    public static class Builder {

        public EntityImplementation build() {
            requireNonNull(implementation.sourceFileLine);
            requireNonNull(implementation.entityDefinitionQualifiedClassName);
            return implementation;
        }

        private EntityImplementation implementation = new EntityImplementation();

        public Builder sourceFileLine(SourceFileLine sourceFileLine) {
            implementation.sourceFileLine = sourceFileLine;
            return this;
        }

        public Builder entityDefinitionQualifiedClassName(Optional<String> entityDefinitionQualifiedClassName) {
            implementation.entityDefinitionQualifiedClassName = entityDefinitionQualifiedClassName;
            return this;
        }

        public Builder storageNames(List<String> storageNames) {
            implementation.storageNames = storageNames;
            return this;
        }
    }

    private EntityImplementation() {

    }
}
