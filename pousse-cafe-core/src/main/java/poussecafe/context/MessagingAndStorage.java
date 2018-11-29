package poussecafe.context;

import java.util.Objects;
import poussecafe.messaging.Messaging;
import poussecafe.storage.Storage;

public class MessagingAndStorage {

    public MessagingAndStorage(Messaging messaging, Storage storage) {
        Objects.requireNonNull(storage);
        this.storage = storage;

        Objects.requireNonNull(messaging);
        this.messaging = messaging;
    }

    private Storage storage;

    public Storage storage() {
        return storage;
    }

    private Messaging messaging;

    public Messaging messaging() {
        return messaging;
    }
}
