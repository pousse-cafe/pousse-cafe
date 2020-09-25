package poussecafe.test.domain.chain2;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.messaging.internal.BaseChainElementAttributes;

@SuppressWarnings("serial")
public class Chain2ElementData
extends BaseChainElementAttributes<Chain2ElementId>
implements Serializable, Chain2Element.Attributes {

    @Override
    public Attribute<Chain2ElementId> identifier() {
        return AttributeBuilder.stringId(Chain2ElementId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;
}
