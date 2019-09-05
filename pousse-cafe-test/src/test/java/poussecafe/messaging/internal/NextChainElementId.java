package poussecafe.messaging.internal;

import poussecafe.domain.chain1.Chain1ElementId;
import poussecafe.domain.chain2.Chain2ElementId;
import poussecafe.domain.chain3.Chain3ElementId;
import poussecafe.util.StringId;

public class NextChainElementId extends StringId {

    public NextChainElementId(String value) {
        super(value);
        typeNumber(value);
    }

    private static int typeNumber(String stringValue) {
        return Integer.parseInt(stringValue.substring(0, 1));
    }

    public int typeNumber() {
        return typeNumber(stringValue());
    }

    public NextChainElementId(Chain1ElementId id) {
        this(1, id.stringValue());
    }

    public NextChainElementId(int typeNumber, String id) {
        super(Integer.toString(typeNumber) + id);
        if(typeNumber < 0 || typeNumber > 9) {
            throw new IllegalArgumentException();
        }
    }

    public NextChainElementId(Chain2ElementId id) {
        this(2, id.stringValue());
    }

    public NextChainElementId(Chain3ElementId id) {
        this(3, id.stringValue());
    }

    public Chain1ElementId toChain1ElementId() {
        return new Chain1ElementId(stringValue().substring(1));
    }

    public Chain2ElementId toChain2ElementId() {
        return new Chain2ElementId(stringValue().substring(1));
    }

    public Chain3ElementId toChain3ElementId() {
        return new Chain3ElementId(stringValue().substring(1));
    }
}
