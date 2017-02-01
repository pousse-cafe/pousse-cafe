package poussecafe.util;

import java.util.UUID;

public class UuidKey extends StringKey {

    public UuidKey() {
        super(UUID.randomUUID().toString());
    }

}
