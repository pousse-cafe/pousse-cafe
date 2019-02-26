package poussecafe.simple.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.simple.domain.MyAggregateKey;
import poussecafe.simple.domain.MyDomainEvent;

@MessageImplementation(message = MyDomainEvent.class)
@SuppressWarnings("serial")
public class MyDomainEventData implements Serializable, MyDomainEvent {

    @Override
    public Attribute<MyAggregateKey> key() {
        return AttributeBuilder.simple(MyAggregateKey.class)
                .from(String.class)
                .adapt(MyAggregateKey::new)
                .get(() -> id)
                .adapt(MyAggregateKey::getValue)
                .set(value -> id = value)
                .build();
    }

    private String id;
}
