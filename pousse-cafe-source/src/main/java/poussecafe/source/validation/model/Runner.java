package poussecafe.source.validation.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.validation.SourceLine;
import poussecafe.source.validation.names.DeclaredComponent;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class Runner
implements Serializable, DeclaredComponent {

    @Override
    public Optional<SourceLine> sourceLine() {
        return Optional.ofNullable(sourceLine);
    }

    private SourceLine sourceLine;

    @Override
    public ClassName className() {
        return className;
    }

    private ClassName className;

    public Set<String> typeParametersQualifiedNames() {
        return typeParametersQualifiedNames;
    }

    private Set<String> typeParametersQualifiedNames = new HashSet<>();

    public static class Builder {

        public Runner build() {
            requireNonNull(runner.sourceLine);
            requireNonNull(runner.className);
            return runner;
        }

        private Runner runner = new Runner();

        public Builder sourceLine(SourceLine sourceLine) {
            runner.sourceLine = sourceLine;
            return this;
        }

        public Builder className(ClassName className) {
            runner.className = className;
            return this;
        }

        public Builder typeParametersQualifiedNames(Collection<String> typeParametersQualifiedNames) {
            runner.typeParametersQualifiedNames.addAll(typeParametersQualifiedNames);
            return this;
        }
    }

    private Runner() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(className, other.className)
                .append(sourceLine, other.sourceLine)
                .append(typeParametersQualifiedNames, other.typeParametersQualifiedNames)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(className)
                .append(sourceLine)
                .append(typeParametersQualifiedNames)
                .build();
    }
}
