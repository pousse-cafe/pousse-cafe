package poussecafe.source.validation.model;

import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.analysis.Name;
import poussecafe.source.validation.SourceLine;
import poussecafe.source.validation.names.NamedComponent;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class ProcessDefinition implements NamedComponent {

    @Override
    public Optional<SourceLine> sourceLine() {
        return Optional.ofNullable(sourceLine);
    }

    private SourceLine sourceLine;

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
            requireNonNull(definition.sourceLine);
            requireNonNull(definition.className);
            requireNonNull(definition.name);
            return definition;
        }

        private ProcessDefinition definition = new ProcessDefinition();

        public Builder sourceLine(SourceLine sourceFileLine) {
            definition.sourceLine = sourceFileLine;
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

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(className, other.className)
                .append(name, other.name)
                .append(sourceLine, other.sourceLine)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(className)
                .append(name)
                .append(sourceLine)
                .build();
    }
}
