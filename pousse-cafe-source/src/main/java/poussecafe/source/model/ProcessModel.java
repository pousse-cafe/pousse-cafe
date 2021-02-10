package poussecafe.source.model;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.Source;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class ProcessModel extends ComponentWithType implements Serializable {

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
