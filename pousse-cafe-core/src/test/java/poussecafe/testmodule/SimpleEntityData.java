package poussecafe.testmodule;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.entity.SimpleEntity;
import poussecafe.util.StringId;

public class SimpleEntityData implements SimpleEntity.Attributes {

    @Override
    public Attribute<StringId> identifier() {
        return AttributeBuilder.single(StringId.class)
                .storedAs(String.class)
                .adaptOnRead(StringId::new)
                .read(() -> id)
                .adaptOnWrite(StringId::stringValue)
                .write(newValue -> id = newValue)
                .build();

    }

    private String id;
}
