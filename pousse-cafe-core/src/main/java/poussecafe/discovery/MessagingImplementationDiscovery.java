package poussecafe.discovery;

import java.util.List;
import java.util.Objects;
import poussecafe.messaging.MessageImplementation;
import poussecafe.messaging.Messaging;

public class MessagingImplementationDiscovery {

    public static class Builder {

        private MessagingImplementationDiscovery discovery = new MessagingImplementationDiscovery();

        public Builder classPathExplorer(ClassPathExplorer classPathExplorer) {
            discovery.classPathExplorer = classPathExplorer;
            return this;
        }

        public Builder messaging(Messaging messaging) {
            discovery.messaging = messaging;
            return this;
        }

        public MessagingImplementationDiscovery build() {
            Objects.requireNonNull(discovery.classPathExplorer);
            Objects.requireNonNull(discovery.messaging);
            return discovery;
        }
    }

    private MessagingImplementationDiscovery() {

    }

    private ClassPathExplorer classPathExplorer;

    public ClassPathExplorer classPathExplorer() {
        return classPathExplorer;
    }

    private Messaging messaging;

    public Messaging messaging() {
        return messaging;
    }

    public List<MessageImplementation> discover() {
        return new MessagingUnitBuilder(messaging)
                .classPathExplorer(classPathExplorer)
                .build()
                .implementations();
    }
}
