package poussecafe.source.validation.model;

import poussecafe.source.validation.SourceFileLine;

import static java.util.Objects.requireNonNull;

public class EntityDefinition {

    private String entityName;

    public String entityName() {
        return entityName;
    }

    private SourceFileLine sourceFileLine;

    public SourceFileLine sourceFileLine() {
        return sourceFileLine;
    }

    public String qualifiedClassName() {
        return qualifiedClassName;
    }

    private String qualifiedClassName;

    public static class Builder {

        public EntityDefinition build() {
            requireNonNull(definition.entityName);
            requireNonNull(definition.sourceFileLine);
            requireNonNull(definition.qualifiedClassName);
            return definition;
        }

        private EntityDefinition definition = new EntityDefinition();

        public Builder entityName(String entityName) {
            definition.entityName = entityName;
            return this;
        }

        public Builder sourceFileLine(SourceFileLine sourceFileLine) {
            definition.sourceFileLine = sourceFileLine;
            return this;
        }

        public Builder qualifiedClassName(String qualifiedClassName) {
            definition.qualifiedClassName = qualifiedClassName;
            return this;
        }
    }

    private EntityDefinition() {

    }
}
