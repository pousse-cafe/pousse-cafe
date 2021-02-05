package poussecafe.source.validation.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.validation.SourceLine;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class Runner implements Serializable {

    private SourceLine sourceFileLine;

    public SourceLine sourceFileLine() {
        return sourceFileLine;
    }

    public String classQualifiedName() {
        return classQualifiedName;
    }

    private String classQualifiedName;

    public Set<String> typeParametersQualifiedNames() {
        return typeParametersQualifiedNames;
    }

    private Set<String> typeParametersQualifiedNames = new HashSet<>();

    public static class Builder {

        public Runner build() {
            requireNonNull(runner.sourceFileLine);
            requireNonNull(runner.classQualifiedName);
            return runner;
        }

        private Runner runner = new Runner();

        public Builder sourceFileLine(SourceLine sourceFileLine) {
            runner.sourceFileLine = sourceFileLine;
            return this;
        }

        public Builder classQualifiedName(String classQualifiedName) {
            runner.classQualifiedName = classQualifiedName;
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
                .append(classQualifiedName, other.classQualifiedName)
                .append(sourceFileLine, other.sourceFileLine)
                .append(typeParametersQualifiedNames, other.typeParametersQualifiedNames)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(classQualifiedName)
                .append(sourceFileLine)
                .append(typeParametersQualifiedNames)
                .build();
    }
}
