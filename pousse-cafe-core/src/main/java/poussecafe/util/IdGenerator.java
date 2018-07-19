package poussecafe.util;

import java.util.UUID;
import poussecafe.domain.Service;

public class IdGenerator implements Service {

    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
