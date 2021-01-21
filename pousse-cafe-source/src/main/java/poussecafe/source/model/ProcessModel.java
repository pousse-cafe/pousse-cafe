package poussecafe.source.model;

import java.util.Optional;
import poussecafe.source.Source;

import static java.util.Objects.requireNonNull;

public class ProcessModel extends ComponentWithType {

    private Source source;

    public Optional<Source> source() {
        return Optional.ofNullable(source);
    }

    public static class Builder {

        private ProcessModel process = new ProcessModel();

        public ProcessModel build() {
            requireNonNull(process.name);
            requireNonNull(process.packageName);
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
