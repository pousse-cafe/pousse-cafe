package poussecafe.myboundedcontext.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.myboundedcontext.domain.YetAnotherDomainEvent;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregateKey;

@MessageImplementation(message = YetAnotherDomainEvent.class)
@SuppressWarnings("serial")
public class YetAnotherDomainEventData implements Serializable, YetAnotherDomainEvent {

    @Override
    public Attribute<MyAggregateKey> key() {
        return AttributeBuilder.stringKey(MyAggregateKey.class)
                .get(() -> id)
                .set(value -> id = value)
                .build();
    }

    private String id;
}
