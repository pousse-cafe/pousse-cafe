package poussecafe.source.validation.model;

import java.util.Optional;
import poussecafe.source.analysis.Name;
import poussecafe.source.validation.SourceFileLine;
import poussecafe.source.validation.names.DeclaredComponent;

import static java.util.Objects.requireNonNull;

public class AggregateComponentDefinition implements DeclaredComponent {

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

    public boolean isInnerClass() {
        return innerClass;
    }

    private boolean innerClass;

    public static class Builder {

        public AggregateComponentDefinition build() {
            requireNonNull(definition.sourceFileLine);
            requireNonNull(definition.className);
            return definition;
        }

        private AggregateComponentDefinition definition = new AggregateComponentDefinition();

        public Builder sourceFileLine(SourceFileLine sourceFileLine) {
            definition.sourceFileLine = Optional.of(sourceFileLine);
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
}
