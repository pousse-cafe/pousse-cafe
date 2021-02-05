package poussecafe.source.model;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.Source;
import poussecafe.source.analysis.SafeClassName;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class TypeComponent implements Serializable {

    private Source source;

    public Source source() {
        return source;
    }

    private SafeClassName typeName;

    public SafeClassName typeName() {
        return typeName;
    }

    public static class Builder {

        private TypeComponent aggregate = new TypeComponent();

        public TypeComponent build() {
            requireNonNull(aggregate.typeName);
            requireNonNull(aggregate.source);
            return aggregate;
        }

        public Builder name(SafeClassName typeName) {
            aggregate.typeName = typeName;
            return this;
        }

        public Builder source(Source source) {
            aggregate.source = source;
            return this;
        }
    }

    private TypeComponent() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(source, other.source)
                .append(typeName, other.typeName)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(source)
                .append(typeName)
                .build();
    }
}
