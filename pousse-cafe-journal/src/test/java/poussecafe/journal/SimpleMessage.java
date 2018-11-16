package poussecafe.journal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.messaging.Message;

import static poussecafe.util.ReferenceEquals.referenceEquals;

public class SimpleMessage implements Message {

    SimpleMessage() {

    }

    public SimpleMessage(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    private String payload;

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(payload)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(payload, other.payload)
                .build());
    }
}
