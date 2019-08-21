package poussecafe.mymodule.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.mymodule.domain.YetAnotherDomainEvent;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;

@MessageImplementation(message = YetAnotherDomainEvent.class)
@SuppressWarnings("serial")
public class YetAnotherDomainEventData implements Serializable, YetAnotherDomainEvent {

    @Override
    public Attribute<MyAggregateId> identifier() {
        return AttributeBuilder.stringId(MyAggregateId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;
}
