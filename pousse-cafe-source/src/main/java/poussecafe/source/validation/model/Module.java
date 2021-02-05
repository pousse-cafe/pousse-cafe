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
public class Module implements NamedComponent {

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

    public String basePackage() {
        return className.qualifier();
    }

    @Override
    public String name() {
        return className.simple();
    }

    public static class Builder {

        public Module build() {
            requireNonNull(module.className);
            return module;
        }

        private Module module = new Module();

        public Builder sourceLine(SourceLine sourceFileLine) {
            module.sourceLine = sourceFileLine;
            return this;
        }

        public Builder className(Name className) {
            module.className = className;
            return this;
        }
    }

    private Module() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(className, other.className)
                .append(sourceLine, other.sourceLine)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(className)
                .append(sourceLine)
                .build();
    }
}
