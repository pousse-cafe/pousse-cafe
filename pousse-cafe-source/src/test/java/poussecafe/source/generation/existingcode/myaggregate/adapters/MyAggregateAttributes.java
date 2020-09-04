package poussecafe.source.generation.existingcode.myaggregate.adapters;

import poussecafe.attribute.Attribute;
import poussecafe.source.generation.existingcode.myaggregate.MyAggregate;
import poussecafe.source.generation.existingcode.myaggregate.MyAggregateId;

import static poussecafe.attribute.AttributeBuilder.stringId;

public class MyAggregateAttributes implements MyAggregate.Attributes {

    @Override
    public Attribute<MyAggregateId> identifier() {
        return stringId(MyAggregateId.class).read(() -> id).write(value -> id = value).build();
    }

    private String id;
}