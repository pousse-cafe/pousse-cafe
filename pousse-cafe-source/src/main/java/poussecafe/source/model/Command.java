package poussecafe.source.model;

import static java.util.Objects.requireNonNull;

public class Command extends ComponentWithType {

    public static class Builder {

        private Command source = new Command();

        public Command build() {
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

    private Command() {

    }
}
