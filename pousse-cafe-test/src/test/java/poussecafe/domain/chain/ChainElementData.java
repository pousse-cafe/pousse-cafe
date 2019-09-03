package poussecafe.domain.chain;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.attribute.adapters.DataAdapters;

@SuppressWarnings("serial")
public class ChainElementData implements Serializable, ChainElement.Attributes {

    @Override
    public Attribute<ChainElementId> identifier() {
        return AttributeBuilder.stringId(ChainElementId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;

    @Override
    public Attribute<Boolean> touched() {
        return AttributeBuilder.single(Boolean.class)
                .read(() -> touched)
                .write(value -> touched = value)
                .build();
    }

    private Boolean touched;

    @Override
    public OptionalAttribute<ChainElementId> next() {
        return AttributeBuilder.optional(ChainElementId.class)
                .usingDataAdapter(DataAdapters.stringId(ChainElementId.class))
                .read(() -> next)
                .write(value -> next = value)
                .build();
    }

    private String next;
}
