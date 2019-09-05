package poussecafe.domain.chain3;

import poussecafe.messaging.internal.ChainElementFactory;

public class Chain3ElementFactory extends ChainElementFactory<Chain3ElementId, Chain3Element.Attributes, Chain3Element> {

    public Chain3ElementFactory() {
        super(3);
    }
}
