package poussecafe.messaging;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class TestMessage implements Serializable, Message {

    private String payload = "data";

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(payload, other.payload)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(payload)
                .build();
    }
}
