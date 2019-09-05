package poussecafe.domain.chain2;

import poussecafe.util.StringId;

public class Chain2ElementId extends StringId {

    public Chain2ElementId(String id) {
        super(id);
    }

    public Chain2ElementId(String chainId, int index) {
        super(chainId + "_" + Integer.toString(index));
    }
}
