package poussecafe.source.model;

import poussecafe.source.Source;
import poussecafe.source.analysis.SafeClassName;

import static java.util.Objects.requireNonNull;

public class TypeComponent {

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
}
