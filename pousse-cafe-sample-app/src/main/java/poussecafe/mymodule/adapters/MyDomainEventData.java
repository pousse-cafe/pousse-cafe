package poussecafe.mymodule.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.mymodule.domain.MyDomainEvent;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;

@MessageImplementation(message = MyDomainEvent.class)
@SuppressWarnings("serial")
public class MyDomainEventData implements Serializable, MyDomainEvent {

    @Override
    public Attribute<MyAggregateId> identifier() {
        return AttributeBuilder.stringId(MyAggregateId.class)
                .read(() -> identifier)
                .write(value -> identifier = value)
                .build();
    }

    private String identifier;
}
