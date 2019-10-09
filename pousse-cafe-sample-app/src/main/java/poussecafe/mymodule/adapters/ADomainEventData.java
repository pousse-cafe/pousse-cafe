package poussecafe.mymodule.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.mymodule.domain.ADomainEvent;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;

@MessageImplementation(message = ADomainEvent.class)
@SuppressWarnings("serial")
public class ADomainEventData implements Serializable, ADomainEvent {

    @Override
    public Attribute<MyAggregateId> identifier() {
        return AttributeBuilder.stringId(MyAggregateId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;
}
