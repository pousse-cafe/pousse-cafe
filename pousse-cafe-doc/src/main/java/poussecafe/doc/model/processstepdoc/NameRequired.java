package poussecafe.doc.model.processstepdoc;

import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.util.ReferenceEquals;

public class NameRequired {

    public static NameRequired required(String name) {
        NameRequired nameRequired = new NameRequired(name);
        nameRequired.required = true;
        return nameRequired;
    }

    private NameRequired(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    private String name;

    public static NameRequired optional(String name) {
        NameRequired nameRequired = new NameRequired(name);
        nameRequired.required = false;
        return nameRequired;
    }

    public String name() {
        return name;
    }

    private boolean required;

    public boolean required() {
        return required;
    }

    @Override
    public boolean equals(Object obj) {
        return ReferenceEquals.referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(name, other.name)
                .append(required, other.required)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(required)
                .build();
    }
}
