package poussecafe.doc.model.entitydoc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;

import static poussecafe.check.Checks.checkThatValue;

public class EntityDocKey {

    public EntityDocKey(AggregateDocKey aggregateDocKey, String name) {
        aggregateDocKey(aggregateDocKey);
        name(name);
    }

    private void aggregateDocKey(AggregateDocKey aggregateDocKey) {
        checkThatValue(aggregateDocKey).notNull();
        this.aggregateDocKey = aggregateDocKey;
    }

    private AggregateDocKey aggregateDocKey;

    public AggregateDocKey aggregateDocKey() {
        return aggregateDocKey;
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
                .append(aggregateDocKey)
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
        EntityDocKey other = (EntityDocKey) obj;
        return new EqualsBuilder()
                .append(aggregateDocKey, other.aggregateDocKey)
                .append(name, other.name)
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(aggregateDocKey)
                .append(name)
                .build();
    }
}
