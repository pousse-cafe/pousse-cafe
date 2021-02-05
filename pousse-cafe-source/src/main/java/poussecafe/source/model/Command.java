package poussecafe.source.model;

import java.io.Serializable;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.Source;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class Command extends ComponentWithType implements Serializable {

    private Source source;

    public Optional<Source> source() {
        return Optional.ofNullable(source);
    }

    public static class Builder {

        private Command command = new Command();

        public Command build() {
            requireNonNull(command.name);
            requireNonNull(command.packageName);
            return command;
        }

        public Builder source(Optional<Source> source) {
            command.source = source.orElse(null);
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

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(source, other.source)
                .append(name, other.name)
                .append(packageName, other.packageName)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(source)
                .append(name)
                .append(packageName)
                .build();
    }
}
