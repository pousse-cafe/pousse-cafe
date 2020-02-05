package poussecafe.jackson;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.messaging.Message;

import static poussecafe.util.ReferenceEquals.referenceEquals;

public class SimpleMessage implements Message {

    public String payload;

    public Object polymorphicPayload;

    public LocalDate date;

    public BigDecimal bigDecimal;

    public int intPrimitive;

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(payload)
                .append(polymorphicPayload)
                .append(date)
                .append(bigDecimal)
                .append(intPrimitive)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(payload, other.payload)
                .append(polymorphicPayload, other.polymorphicPayload)
                .append(date, other.date)
                .append(bigDecimal, other.bigDecimal)
                .append(intPrimitive, other.intPrimitive)
                .build());
    }
}
