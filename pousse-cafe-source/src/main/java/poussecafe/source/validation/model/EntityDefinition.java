package poussecafe.source.validation.model;

import java.util.Optional;
import poussecafe.source.analysis.Name;
import poussecafe.source.validation.SourceFileLine;
import poussecafe.source.validation.names.NamedComponent;

import static java.util.Objects.requireNonNull;

public class EntityDefinition implements NamedComponent {

    private String entityName;

    @Override
    public String name() {
        return entityName;
    }

    private Optional<SourceFileLine> sourceFileLine;

    @Override
    public Optional<SourceFileLine> sourceFileLine() {
        return sourceFileLine;
    }

    @Override
    public Name className() {
        return className;
    }

    private Name className;

    public static class Builder {

        public EntityDefinition build() {
            requireNonNull(definition.entityName);
            requireNonNull(definition.sourceFileLine);
            requireNonNull(definition.className);
            return definition;
        }

        private EntityDefinition definition = new EntityDefinition();

        public Builder entityName(String entityName) {
            definition.entityName = entityName;
            return this;
        }

        public Builder sourceFileLine(SourceFileLine sourceFileLine) {
            definition.sourceFileLine = Optional.of(sourceFileLine);
            return this;
        }

        public Builder className(Name className) {
            definition.className = className;
            return this;
        }
    }

    private EntityDefinition() {

    }
}
