package poussecafe.doc.model;

import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static poussecafe.util.Equality.referenceEquals;

public class UbiquitousLanguageEntry
        implements Comparable<UbiquitousLanguageEntry> {

    public static class Builder {

        private UbiquitousLanguageEntry entry = new UbiquitousLanguageEntry();

        public Builder moduleName(String moduleName) {
            entry.moduleName = Optional.of(moduleName);
            return this;
        }

        public Builder componentDoc(ComponentDoc componentDoc) {
            entry.componentDoc = componentDoc;
            return this;
        }

        public Builder type(String type) {
            entry.type = type;
            return this;
        }

        public UbiquitousLanguageEntry build() {
            Objects.requireNonNull(entry.type);
            Objects.requireNonNull(entry.componentDoc);
            return entry;
        }
    }

    private UbiquitousLanguageEntry() {

    }

    private Optional<String> moduleName = Optional.empty();

    public Optional<String> moduleName() {
        return moduleName;
    }

    private ComponentDoc componentDoc;

    public ComponentDoc componentDoc() {
        return componentDoc;
    }

    private String type;

    public String getType() {
        return type;
    }

    public String qualifiedName() {
        Optional<String> optionalModuleName = moduleName();
        if(optionalModuleName.isPresent()) {
            return componentDoc().name() + " (" + optionalModuleName.get() + ")";
        } else {
            return componentDoc().name();
        }
    }

    @Override
    public int compareTo(UbiquitousLanguageEntry o) {
        return comparisonIndex().compareTo(o.comparisonIndex());
    }

    private String comparisonIndex() {
        return qualifiedName() + type;
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(comparisonIndex(), other.comparisonIndex())
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(comparisonIndex())
                .build();
    }
}
