package poussecafe.source.model;

import static java.util.Objects.requireNonNull;

public class ProcessModel {

    private String name;

    public String name() {
        return name;
    }

    private String packageName;

    public String packageName() {
        return packageName;
    }

    public static class Builder {

        private ProcessModel source = new ProcessModel();

        public ProcessModel build() {
            requireNonNull(source.name);
            requireNonNull(source.packageName);
            return source;
        }

        public Builder name(String name) {
            source.name = name;
            return this;
        }

        public Builder packageName(String packageName) {
            source.packageName = packageName;
            return this;
        }
    }

    private ProcessModel() {

    }
}
