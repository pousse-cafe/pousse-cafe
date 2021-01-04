package poussecafe.source.validation.model;

import poussecafe.source.analysis.Name;
import poussecafe.source.validation.SourceFileLine;
import poussecafe.source.validation.names.NamedComponent;

import static java.util.Objects.requireNonNull;

public class ProcessDefinition implements NamedComponent {

    @Override
    public SourceFileLine sourceFileLine() {
        return sourceFileLine;
    }

    private SourceFileLine sourceFileLine;

    @Override
    public Name className() {
        return className;
    }

    private Name className;

    @Override
    public String name() {
        return name;
    }

    private String name;

    public static class Builder {

        public ProcessDefinition build() {
            requireNonNull(definition.sourceFileLine);
            requireNonNull(definition.className);
            requireNonNull(definition.name);
            return definition;
        }

        private ProcessDefinition definition = new ProcessDefinition();

        public Builder sourceFileLine(SourceFileLine sourceFileLine) {
            definition.sourceFileLine = sourceFileLine;
            return this;
        }

        public Builder className(Name className) {
            definition.className = className;
            return this;
        }

        public Builder name(String name) {
            definition.name = name;
            return this;
        }
    }

    private ProcessDefinition() {

    }
}
