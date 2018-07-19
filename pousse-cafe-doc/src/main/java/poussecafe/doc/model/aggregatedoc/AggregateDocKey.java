package poussecafe.doc.model.aggregatedoc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static poussecafe.check.Checks.checkThatValue;

public class AggregateDocKey {

    public AggregateDocKey(String boundedContextKey, String name) {
        boundedContextKey(boundedContextKey);
        name(name);
    }

    private void boundedContextKey(String boundedContextKey) {
        checkThatValue(boundedContextKey).notNull();
        this.boundedContextKey = boundedContextKey;
    }

    private String boundedContextKey;

    public String boundedContextKey() {
        return boundedContextKey;
    }

    private void name(String name) {
        checkThatValue(name).notNull();
        this.name = name;
    }

    private String name;

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(boundedContextKey)
                .append(name)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        AggregateDocKey other = (AggregateDocKey) obj;
        return new EqualsBuilder()
                .append(boundedContextKey, other.boundedContextKey)
                .append(name, other.name)
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(boundedContextKey)
                .append(name)
                .build();
    }
}
