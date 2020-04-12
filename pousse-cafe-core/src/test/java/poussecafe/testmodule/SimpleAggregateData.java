package poussecafe.testmodule;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;

@SuppressWarnings("serial")
public class SimpleAggregateData implements SimpleAggregate.Attributes, Serializable {

    @Override
    public Attribute<SimpleAggregateId> identifier() {
        return new Attribute<>() {
            @Override
            public SimpleAggregateId value() {
                return new SimpleAggregateId(id);
            }

            @Override
            public void value(SimpleAggregateId value) {
                id = value.stringValue();
            }
        };
    }

    private String id;

    @Override
    public Attribute<String> data() {
        return AttributeBuilder.single(String.class)
                .read(() -> data)
                .write(value -> data = value)
                .build();
    }

    private String data;
}
