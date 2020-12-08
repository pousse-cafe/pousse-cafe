package poussecafe.source.generation.generatedfull.model.aggregate1.adapters;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.source.generation.generatedfull.model.aggregate1.Aggregate1.Root;
import poussecafe.source.generation.generatedfull.model.aggregate1.Aggregate1Id;

public class Aggregate1Attributes implements Root.Attributes {

    @Override
    public Attribute<Aggregate1Id> identifier() {
        return AttributeBuilder
                .stringId(Aggregate1Id.class)
                .read(() -> identifier)
                .write(value -> identifier = value)
                .build();
    }

    private String identifier;

    @SuppressWarnings("unused")
    private Long version;
}