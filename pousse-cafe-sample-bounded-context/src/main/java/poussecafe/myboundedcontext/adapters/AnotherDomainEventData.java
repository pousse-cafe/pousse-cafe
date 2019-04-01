package poussecafe.myboundedcontext.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.myboundedcontext.domain.AnotherDomainEvent;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregateKey;

@MessageImplementation(message = AnotherDomainEvent.class)
@SuppressWarnings("serial")
public class AnotherDomainEventData implements Serializable, AnotherDomainEvent {

    @Override
    public Attribute<MyAggregateKey> key() {
        return AttributeBuilder.stringKey(MyAggregateKey.class)
                .get(() -> id)
                .set(value -> id = value)
                .build();
    }

    private String id;

    @Override
    public Attribute<Integer> x() {
        return AttributeBuilder.single(Integer.class)
                .get(() -> x)
                .set(value -> x = value)
                .build();
    }

    private int x;
}
