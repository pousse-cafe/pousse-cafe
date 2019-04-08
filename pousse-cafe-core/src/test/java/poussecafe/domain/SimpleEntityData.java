package poussecafe.domain;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.entity.SimpleEntity;
import poussecafe.util.StringId;

public class SimpleEntityData implements SimpleEntity.Attributes {

    @Override
    public Attribute<StringId> identifier() {
        return AttributeBuilder.single(StringId.class)
                .from(String.class)
                .adapt(StringId::new)
                .get(() -> id)
                .adapt(StringId::stringValue)
                .set(newValue -> id = newValue)
                .build();

    }

    private String id;
}
