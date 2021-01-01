package poussecafe.source.validation.entity.adapters;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.DataImplementation;
import poussecafe.source.validation.entity.MyEntity;

import static poussecafe.attribute.AttributeBuilder.single;

@DataImplementation(entity = MyEntity.class)
public class MyEntityAttributes implements MyEntity.Attributes {

    @Override
    public Attribute<String> identifier() {
        return single(String.class)
                .read(() -> identifier)
                .write(value -> identifier = value)
                .build();
    }

    private String identifier;
}
