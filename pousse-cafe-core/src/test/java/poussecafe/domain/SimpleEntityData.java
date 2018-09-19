package poussecafe.domain;

import poussecafe.storable.Property;
import poussecafe.storable.PropertyBuilder;
import poussecafe.storable.SimpleEntity;
import poussecafe.util.StringKey;

public class SimpleEntityData implements SimpleEntity.Data {

    @Override
    public Property<StringKey> key() {
        return PropertyBuilder.simple(StringKey.class)
                .from(String.class)
                .adapt(StringKey::new)
                .get(() -> key)
                .adapt(StringKey::getValue)
                .set(newValue -> key = newValue)
                .build();

    }

    private String key;
}
