package poussecafe.source.validation.entity.adapters;

import poussecafe.attribute.Attribute;
import poussecafe.source.validation.entity.MyAggregateRoot;

import static poussecafe.attribute.AttributeBuilder.single;

public class MyAggregateRootAttributes implements MyAggregateRoot.Attributes {

    @Override
    public Attribute<String> identifier() {
        return single(String.class)
                .read(() -> identifier)
                .write(value -> identifier = value)
                .build();
    }

    private String identifier;
}
