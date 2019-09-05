package poussecafe.domain.chain1;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.messaging.internal.BaseChainElementAttributes;

@SuppressWarnings("serial")
public class Chain1ElementData
extends BaseChainElementAttributes<Chain1ElementId>
implements Serializable, Chain1Element.Attributes {

    @Override
    public Attribute<Chain1ElementId> identifier() {
        return AttributeBuilder.stringId(Chain1ElementId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;
}
