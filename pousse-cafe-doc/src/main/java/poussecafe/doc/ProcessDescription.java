package poussecafe.doc;

import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static poussecafe.util.ReferenceEquals.referenceEquals;

public class ProcessDescription {

    public static ProcessDescription parse(String text) {
        ProcessDescription description = new ProcessDescription();
        String[] words = text.split("\\s+", 2);
        description.name(words[0]);
        description.description(words[1]);
        return description;
    }

    private ProcessDescription() {

    }

    private void name(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    private String name;

    public String name() {
        return name;
    }

    private void description(String description) {
        Objects.requireNonNull(description);
        this.description = description;
    }

    private String description;

    public String description() {
        return description;
    }

    public static class Builder {

        private ProcessDescription description = new ProcessDescription();

        public Builder name(String name) {
            description.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description.description = description;
            return this;
        }

        public ProcessDescription build() {
            Objects.requireNonNull(description.name);
            Objects.requireNonNull(description.description);
            return description;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(name, other.name)
                .append(description, other.description)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(description)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(name)
                .append(description)
                .build();
    }
}
