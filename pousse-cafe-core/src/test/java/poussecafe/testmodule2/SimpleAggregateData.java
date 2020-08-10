package poussecafe.testmodule2;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.testmodule2.SimpleAggregate.SimpleAggregateRoot;

@SuppressWarnings("serial")
public class SimpleAggregateData implements SimpleAggregateRoot.Attributes, Serializable {

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
