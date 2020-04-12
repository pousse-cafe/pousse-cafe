package poussecafe.testmodule;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.domain.DomainEvent;

@MessageImplementation(message = TestDomainEvent4.class)
public class TestDomainEvent4 implements DomainEvent {

    public Attribute<SimpleAggregateId> identifier() {
        return AttributeBuilder.stringId(SimpleAggregateId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;
}
