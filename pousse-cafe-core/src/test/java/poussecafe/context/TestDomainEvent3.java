package poussecafe.context;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.SimpleAggregateKey;

public class TestDomainEvent3 implements DomainEvent {

    public Attribute<SimpleAggregateKey> key() {
        return AttributeBuilder.stringKey(SimpleAggregateKey.class)
                .get(() -> key)
                .set(value -> key = value)
                .build();
    }

    private String key;
}
