package poussecafe.process;

import java.util.UUID;
import poussecafe.util.StringKey;

public class SimpleProcessManagerKey extends StringKey {

    public SimpleProcessManagerKey() {
        super(UUID.randomUUID().toString());
    }

}
