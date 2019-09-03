package poussecafe.domain.chain;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.domain.DomainEvent;

@MessageImplementation(message = ChainElementTouchable.class)
@SuppressWarnings("serial")
public class ChainElementTouchable implements Serializable, DomainEvent {

    public Attribute<ChainElementId> next() {
        return AttributeBuilder.stringId(ChainElementId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;
}
