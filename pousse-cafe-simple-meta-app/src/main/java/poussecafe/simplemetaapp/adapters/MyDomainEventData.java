package poussecafe.simplemetaapp.adapters;

import java.io.Serializable;
import poussecafe.messaging.MessageImplementation;
import poussecafe.property.Property;
import poussecafe.property.PropertyBuilder;
import poussecafe.simplemetaapp.domain.MyAggregateKey;
import poussecafe.simplemetaapp.domain.MyDomainEvent;

@MessageImplementation(message = MyDomainEvent.class)
@SuppressWarnings("serial")
public class MyDomainEventData implements Serializable, MyDomainEvent {

    @Override
    public Property<MyAggregateKey> key() {
        return PropertyBuilder.simple(MyAggregateKey.class)
                .from(String.class)
                .adapt(MyAggregateKey::new)
                .get(() -> id)
                .adapt(MyAggregateKey::getValue)
                .set(value -> id = value)
                .build();
    }

    private String id;
}
