package poussecafe.test.domain.chain1;

import poussecafe.util.StringId;

public class Chain1ElementId extends StringId {

    public Chain1ElementId(String id) {
        super(id);
    }

    public Chain1ElementId(String chainId, int index) {
        super(chainId + "_" + Integer.toString(index));
    }
}
