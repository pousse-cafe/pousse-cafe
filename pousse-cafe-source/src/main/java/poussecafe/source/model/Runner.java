package poussecafe.source.model;

import java.util.Optional;
import poussecafe.source.Source;

import static java.util.Objects.requireNonNull;

public class Runner {

    public Optional<Source> runnerSource() {
        return runnerSource;
    }

    private Optional<Source> runnerSource = Optional.empty();

    public String className() {
        return className;
    }

    private String className;

    public static class Builder {

        private Runner messageListener = new Runner();

        public Runner build() {
            requireNonNull(messageListener.runnerSource);
            requireNonNull(messageListener.className);
            return messageListener;
        }

        public Builder withRunnerSource(Optional<Source> runnerSource) {
            messageListener.runnerSource = runnerSource;
            return this;
        }

        public Builder withClassName(String className) {
            messageListener.className = className;
            return this;
        }
    }

    private Runner() {

    }
}
