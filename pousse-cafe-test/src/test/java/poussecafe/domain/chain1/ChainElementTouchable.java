package poussecafe.domain.chain1;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.domain.DomainEvent;
import poussecafe.messaging.internal.NextChainElementId;

@MessageImplementation(message = ChainElementTouchable.class)
@SuppressWarnings("serial")
public class ChainElementTouchable implements Serializable, DomainEvent {

    public Attribute<NextChainElementId> next() {
        return AttributeBuilder.stringId(NextChainElementId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;
}
