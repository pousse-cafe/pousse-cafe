package poussecafe.source.model;

import poussecafe.source.Source;

import static java.util.Objects.requireNonNull;

public class Runner {

    public Source runnerSource() {
        return runnerSource;
    }

    private Source runnerSource;

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

        public Builder withRunnerSource(Source runnerSource) {
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
