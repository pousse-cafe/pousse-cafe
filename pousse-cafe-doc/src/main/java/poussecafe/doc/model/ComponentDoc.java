package poussecafe.doc.model;

import java.util.Optional;
import poussecafe.domain.ValueObject;

import static poussecafe.check.Checks.checkThatValue;

public class ComponentDoc implements ValueObject {

    public static class Builder {

        private ComponentDoc doc = new ComponentDoc();

        public Builder name(String name) {
            doc.name = name;
            return this;
        }

        public Builder description(String description) {
            doc.description = description;
            return this;
        }

        public Builder shortDescription(Optional<String> shortDescription) {
            doc.shortDescription = shortDescription;
            return this;
        }

        public Builder trivial(boolean trivial) {
            doc.trivial = trivial;
            return this;
        }

        public ComponentDoc build() {
            checkThatValue(doc.name).notNull();
            checkThatValue(doc.description).notNull();
            checkThatValue(doc.shortDescription).notNull();
            return doc;
        }
    }

    private ComponentDoc() {

    }

    private String name;

    public String name() {
        return name;
    }

    private String description;

    public String description() {
        return description;
    }

    private Optional<String> shortDescription = Optional.empty();

    public Optional<String> shortDescription() {
        return shortDescription;
    }

    private boolean trivial;

    public boolean trivial() {
        return trivial;
    }
}
