package poussecafe.collection;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("unchecked")
public class Pair<E1, E2> {

    public Pair(E1 value1, E2 value2) {
        array = new Object[2];
        array[0] = value1;
        array[1] = value2;
    }

    private Object[] array;

    public E1 one() {
        return (E1) array[0];
    }

    public E2 two() {
        return (E2) array[1];
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(array[0])
                .append(array[1])
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(array[0])
                .append(array[1])
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(array[0], other.array[0])
                .append(array[1], other.array[1])
                .build());
    }
}
