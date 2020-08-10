package poussecafe.testmodule2;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.discovery.MessageImplementation;

import static poussecafe.attribute.AttributeBuilder.single;
import static poussecafe.attribute.AttributeBuilder.stringId;

@MessageImplementation(message = CreateSimpleAggregate.class)
@SuppressWarnings("serial")
public class CreateSimpleAggregateData implements Serializable, CreateSimpleAggregate {

    @Override
    public Attribute<SimpleAggregateId> identifier() {
        return stringId(SimpleAggregateId.class)
                .read(() -> identifier)
                .write(value -> identifier = value)
                .build();
    }

    private String identifier;

    @Override
    public Attribute<String> data() {
        return single(String.class)
                .read(() -> data)
                .write(value -> data = value)
                .build();
    }

    private String data;
}
