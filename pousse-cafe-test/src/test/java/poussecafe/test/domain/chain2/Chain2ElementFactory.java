package poussecafe.test.domain.chain2;

import poussecafe.messaging.internal.ChainElementFactory;

public class Chain2ElementFactory extends ChainElementFactory<Chain2ElementId, Chain2Element.Attributes, Chain2Element> {

    public Chain2ElementFactory() {
        super(2);
    }
}
