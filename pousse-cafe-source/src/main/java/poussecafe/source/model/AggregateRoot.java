package poussecafe.source.model;

import java.nio.file.Path;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class AggregateRoot {

    private String name;

    public String name() {
        return name;
    }

    private Path filePath;

    public Path filePath() {
        return filePath;
    }

    public static class Builder {

        private AggregateRoot source = new AggregateRoot();

        public AggregateRoot build() {
            requireNonNull(source.name);
            requireNonNull(source.filePath);
            return source;
        }

        public Builder name(String name) {
            source.name = name;
            return this;
        }

        public Optional<String> name() {
            return Optional.ofNullable(source.name);
        }

        public Builder filePath(Path filePath) {
            source.filePath = filePath;
            return this;
        }
    }

    private AggregateRoot() {

    }
}
