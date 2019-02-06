package poussecafe.simplemetaapp.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.contextconfigurer.MessageImplementation;
import poussecafe.simplemetaapp.domain.MyAggregateKey;
import poussecafe.simplemetaapp.domain.MyDomainEvent;

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
