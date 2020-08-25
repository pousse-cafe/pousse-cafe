package poussecafe.source.model;

import static java.util.Objects.requireNonNull;

public class ProcessModel {

    private String name;

    public String name() {
        return name;
    }

    public static class Builder {

        private ProcessModel source = new ProcessModel();

        public ProcessModel build() {
            requireNonNull(source.name);
            return source;
        }

        public Builder name(String name) {
            source.name = name;
            return this;
        }
    }

    private ProcessModel() {

    }
}
