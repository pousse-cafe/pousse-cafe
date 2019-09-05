package poussecafe.domain.chain3;

import poussecafe.util.StringId;

public class Chain3ElementId extends StringId {

    public Chain3ElementId(String id) {
        super(id);
    }

    public Chain3ElementId(String chainId, int index) {
        super(chainId + "_" + Integer.toString(index));
    }
}
