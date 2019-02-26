package poussecafe.discovery;

import java.util.Collections;
import java.util.List;
import poussecafe.environment.EntityImplementation;
import poussecafe.storage.Storage;

public class StorageUnit {

    StorageUnit() {

    }

    Storage storage;

    public Storage storage() {
        return storage;
    }

    List<EntityImplementation> implementations;

    public List<EntityImplementation> implementations() {
        return Collections.unmodifiableList(implementations);
    }
}
