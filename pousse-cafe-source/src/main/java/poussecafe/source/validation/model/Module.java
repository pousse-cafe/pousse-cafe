package poussecafe.source.validation.model;

import java.util.Optional;
import poussecafe.source.analysis.Name;
import poussecafe.source.validation.SourceFileLine;
import poussecafe.source.validation.names.NamedComponent;

import static java.util.Objects.requireNonNull;

public class Module implements NamedComponent {

    @Override
    public Optional<SourceFileLine> sourceFileLine() {
        return sourceFileLine;
    }

    private Optional<SourceFileLine> sourceFileLine = Optional.empty();

    @Override
    public Name className() {
        return name;
    }

    private Name name;

    public String basePackage() {
        return name.qualifier();
    }

    @Override
    public String name() {
        return name.simple();
    }

    public static class Builder {

        public Module build() {
            requireNonNull(module.sourceFileLine);
            requireNonNull(module.name);
            return module;
        }

        private Module module = new Module();

        public Builder sourceFileLine(SourceFileLine sourceFileLine) {
            module.sourceFileLine = Optional.of(sourceFileLine);
            return this;
        }

        public Builder name(Name name) {
            module.name = name;
            return this;
        }
    }

    private Module() {

    }
}