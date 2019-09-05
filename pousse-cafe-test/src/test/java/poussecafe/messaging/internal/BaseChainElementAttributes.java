package poussecafe.messaging.internal;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.attribute.adapters.DataAdapters;

@SuppressWarnings("serial")
public abstract class BaseChainElementAttributes<I> implements Serializable, ChainElementAttributes<I> {

    @Override
    public Attribute<Boolean> touched() {
        return AttributeBuilder.single(Boolean.class)
                .read(() -> touched)
                .write(value -> touched = value)
                .build();
    }

    private Boolean touched;

    @Override
    public OptionalAttribute<NextChainElementId> next() {
        return AttributeBuilder.optional(NextChainElementId.class)
                .usingDataAdapter(DataAdapters.stringId(NextChainElementId.class))
                .read(() -> next)
                .write(value -> next = value)
                .build();
    }

    private String next;
}
