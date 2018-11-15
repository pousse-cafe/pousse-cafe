package poussecafe.simplemetaapp.adapters;

import poussecafe.messaging.MessageImplementation;
import poussecafe.property.Property;
import poussecafe.property.PropertyBuilder;
import poussecafe.simplemetaapp.domain.MyAggregateKey;
import poussecafe.simplemetaapp.domain.MyDomainEvent;

@MessageImplementation(message = MyDomainEvent.class)
public class MyDomainEventData implements MyDomainEvent {

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
