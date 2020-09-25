package poussecafe.source.generation.generatedinternal.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.source.generation.generatedinternal.MyAggregate;
import poussecafe.source.generation.generatedinternal.MyAggregateId;

@SuppressWarnings("serial")
public class MyAggregateAttributes implements Serializable, MyAggregate.Attributes {

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