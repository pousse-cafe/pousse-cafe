package poussecafe.source.model;

import poussecafe.source.Source;

import static java.util.Objects.requireNonNull;

public class ProcessModel extends ComponentWithType {

    private Source source;

    public Source source() {
        return source;
    }

    public static class Builder {

        private ProcessModel process = new ProcessModel();

        public ProcessModel build() {
            requireNonNull(process.name);
            requireNonNull(process.packageName);
            requireNonNull(process.source);
            return process;
        }

        public Builder source(Source source) {
            process.source = source;
            return this;
        }

        public Builder name(String name) {
            process.name = name;
            return this;
        }

        public Builder packageName(String packageName) {
            process.packageName = packageName;
            return this;
        }
    }

    private ProcessModel() {

    }
}
