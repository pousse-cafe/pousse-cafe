package poussecafe.mymodule.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.mymodule.domain.AnotherDomainEvent;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;

@MessageImplementation(message = AnotherDomainEvent.class)
@SuppressWarnings("serial")
public class AnotherDomainEventData implements Serializable, AnotherDomainEvent {

    @Override
    public Attribute<MyAggregateId> identifier() {
        return AttributeBuilder.stringId(MyAggregateId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;

    @Override
    public Attribute<Integer> x() {
        return AttributeBuilder.single(Integer.class)
                .read(() -> x)
                .write(value -> x = value)
                .build();
    }

    private int x;
}
