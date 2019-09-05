package poussecafe.domain.chain3;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.messaging.internal.BaseChainElementAttributes;

@SuppressWarnings("serial")
public class Chain3ElementData
extends BaseChainElementAttributes<Chain3ElementId>
implements Serializable, Chain3Element.Attributes {

    @Override
    public Attribute<Chain3ElementId> identifier() {
        return AttributeBuilder.stringId(Chain3ElementId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;
}
