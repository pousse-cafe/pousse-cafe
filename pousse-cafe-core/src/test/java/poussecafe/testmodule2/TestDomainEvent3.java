package poussecafe.testmodule2;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.domain.DomainEvent;

@SuppressWarnings("serial")
@MessageImplementation(message = TestDomainEvent3.class)
public class TestDomainEvent3 implements DomainEvent, Serializable {

    public Attribute<SimpleAggregateId> identifier() {
        return AttributeBuilder.stringId(SimpleAggregateId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;
}
