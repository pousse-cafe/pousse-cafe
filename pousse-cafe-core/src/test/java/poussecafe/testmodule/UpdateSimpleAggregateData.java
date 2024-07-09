package poussecafe.testmodule;

import static poussecafe.attribute.AttributeBuilder.stringId;

import java.io.Serializable;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.MessageImplementation;

@MessageImplementation(message = UpdateSimpleAggregate.class)
@SuppressWarnings("serial")
public class UpdateSimpleAggregateData implements Serializable, UpdateSimpleAggregate {

    @Override
    public Attribute<SimpleAggregateId> identifier() {
        return stringId(SimpleAggregateId.class)
                .read(() -> identifier)
                .write(value -> identifier = value)
                .build();
    }

    private String identifier;
}
