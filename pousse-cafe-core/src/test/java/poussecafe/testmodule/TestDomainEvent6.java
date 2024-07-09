package poussecafe.testmodule;

import static poussecafe.attribute.AttributeBuilder.stringId;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.domain.DomainEvent;

@SuppressWarnings("serial")
@MessageImplementation(message = TestDomainEvent6.class)
public class TestDomainEvent6 implements DomainEvent, Serializable {

    public Attribute<SimpleAggregateId> identifier() {
        return stringId(SimpleAggregateId.class)
                .read(() -> identifier)
                .write(value -> identifier = value)
                .build();
    }

    private String identifier;
}
