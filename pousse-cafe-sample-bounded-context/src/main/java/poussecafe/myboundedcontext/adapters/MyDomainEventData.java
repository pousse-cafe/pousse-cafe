package poussecafe.myboundedcontext.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.myboundedcontext.domain.MyDomainEvent;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregateKey;

@MessageImplementation(message = MyDomainEvent.class)
@SuppressWarnings("serial")
public class MyDomainEventData implements Serializable, MyDomainEvent {

    @Override
    public Attribute<MyAggregateKey> key() {
        return AttributeBuilder.stringKey(MyAggregateKey.class)
                .get(() -> id)
                .set(value -> id = value)
                .build();
    }

    private String id;
}
