package poussecafe.context;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.SimpleAggregateId;

public class TestDomainEvent4 implements DomainEvent {

    public Attribute<SimpleAggregateId> identifier() {
        return AttributeBuilder.stringId(SimpleAggregateId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;
}
