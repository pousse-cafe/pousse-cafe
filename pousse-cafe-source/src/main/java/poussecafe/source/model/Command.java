package poussecafe.source.model;

import java.util.Optional;
import poussecafe.source.Source;

import static java.util.Objects.requireNonNull;

public class Command extends ComponentWithType {

    private Optional<Source> source = Optional.empty();

    public Optional<Source> source() {
        return source;
    }

    public static class Builder {

        private Command command = new Command();

        public Command build() {
            requireNonNull(command.name);
            requireNonNull(command.packageName);
            requireNonNull(command.source);
            return command;
        }

        public Builder source(Optional<Source> source) {
            command.source = source;
            return this;
        }

        public Builder name(String name) {
            command.name = name;
            return this;
        }

        public Builder packageName(String packageName) {
            command.packageName = packageName;
            return this;
        }
    }

    private Command() {

    }
}
