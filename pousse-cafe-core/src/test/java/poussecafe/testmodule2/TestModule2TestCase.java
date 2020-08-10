package poussecafe.testmodule2;

import java.time.Duration;
import java.util.Optional;
import org.junit.Before;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessagingConnection;
import poussecafe.messaging.internal.InternalMessagingQueue.InternalMessageReceiver;
import poussecafe.runtime.Runtime;

public abstract class TestModule2TestCase {

    @Before
    public void configureContext() {
        runtime = new Runtime.Builder()
                .withBundle(TestModule2Bundle.configurer().defineAndImplementDefault().build())
                .build();
        runtime.injector().injectDependenciesInto(this);
        runtime.registerListenersOf(this);
        runtime.start();
    }

    private Runtime runtime;

    public Runtime runtime() {
        return runtime;
    }

    public void waitUntilEndOfMessageProcessing() {
        waitUntilEndOfMessageProcessingOfElapsed(Duration.ofSeconds(5));
    }

    public void waitUntilEndOfMessageProcessingOfElapsed(Duration maxWaitTime) {
        for(MessagingConnection connection : runtime().messagingConnections()) {
            @SuppressWarnings("rawtypes")
            MessageReceiver receiver = connection.messageReceiver();
            if(receiver instanceof InternalMessageReceiver) {
                InternalMessageReceiver internalMessageReceiver = (InternalMessageReceiver) receiver;
                internalMessageReceiver.queue().waitUntilEmptyOrInterrupted(Duration.ofSeconds(1), Optional.of(maxWaitTime));
            }
        }
    }
}
