package poussecafe.domain;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.entity.SimpleEntity;
import poussecafe.util.StringKey;

public class SimpleEntityData implements SimpleEntity.Attributes {

    @Override
    public Attribute<StringKey> key() {
        return AttributeBuilder.single(StringKey.class)
                .from(String.class)
                .adapt(StringKey::new)
                .get(() -> key)
                .adapt(StringKey::getValue)
                .set(newValue -> key = newValue)
                .build();

    }

    private String key;
}
