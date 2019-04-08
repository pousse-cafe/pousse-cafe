package poussecafe.domain;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;

public class SimpleAggregateData implements SimpleAggregate.Attributes {

    @Override
    public Attribute<SimpleAggregateId> identifier() {
        return new Attribute<SimpleAggregateId>() {
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
                .get(() -> data)
                .set(value -> data = value)
                .build();
    }

    private String data;
}
