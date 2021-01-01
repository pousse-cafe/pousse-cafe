package poussecafe.source.validation.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import poussecafe.source.validation.SourceFileLine;

import static java.util.Objects.requireNonNull;

public class Runner {

    private SourceFileLine sourceFileLine;

    public SourceFileLine sourceFileLine() {
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

        public Builder sourceFileLine(SourceFileLine sourceFileLine) {
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
}
