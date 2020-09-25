package poussecafe.source.generation.generated.adapters;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.source.generation.generated.MyAggregate;
import poussecafe.source.generation.generated.MyAggregateId;

public class MyAggregateAttributes implements MyAggregate.Attributes {

    @Override
    public Attribute<MyAggregateId> identifier() {
        return AttributeBuilder
                .stringId(MyAggregateId.class)
                .read(() -> identifier)
                .write(value -> identifier = value)
                .build();
    }

    private String identifier;

    @SuppressWarnings("unused")
    private Long version;
}