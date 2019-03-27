package poussecafe.runtime;

import java.util.Objects;

public class RuntimeFriend {

    public RuntimeFriend(Runtime runtime) {
        Objects.requireNonNull(runtime);
        this.runtime = runtime;
    }

    private Runtime runtime;

    public MessageSenderLocator messageSenderLocator() {
        return runtime.messageSenderLocator();
    }
}
