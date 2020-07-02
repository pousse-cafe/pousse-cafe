package poussecafe.source.model;

import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

public class ProcessModel {

    private String name;

    public String name() {
        return name;
    }

    private Path filePath;

    public Path filePath() {
        return filePath;
    }

    public static class Builder {

        private ProcessModel source = new ProcessModel();

        public ProcessModel build() {
            requireNonNull(source.name);
            requireNonNull(source.filePath);
            return source;
        }

        public Builder name(String name) {
            source.name = name;
            return this;
        }

        public Builder filePath(Path filePath) {
            source.filePath = filePath;
            return this;
        }
    }

    private ProcessModel() {

    }
}
