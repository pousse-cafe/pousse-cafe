package poussecafe.domain.chain1;

import poussecafe.messaging.internal.ChainElementFactory;

public class Chain1ElementFactory extends ChainElementFactory<Chain1ElementId, Chain1Element.Attributes, Chain1Element> {

    public Chain1ElementFactory() {
        super(1);
    }
}
