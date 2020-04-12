package poussecafe.process.updateorcreate;

import java.time.Duration;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessagingConnection;
import poussecafe.messaging.internal.InternalMessagingQueue.InternalMessageReceiver;
import poussecafe.runtime.Runtime;
import poussecafe.testmodule.SimpleAggregateId;
import poussecafe.testmodule.TestDomainEvent3;
import poussecafe.testmodule.TestModuleBundle;

import static org.junit.Assert.assertTrue;

public class UpdateOrCreateTest {

    @Before
    public void configureContext() {
        runtime = new Runtime.Builder()
                .withBundle(TestModuleBundle.configurer().defineAndImplementDefault().build())
                .build();
        runtime.injector().injectDependenciesInto(this);
        runtime.registerListenersOf(this);
        runtime.start();
    }

    private Runtime runtime;

    @Test
    public void throwingRunnerDoesNotTriggerInfiniteLoop() {
        TestDomainEvent3 event = runtime.newDomainEvent(TestDomainEvent3.class);
        event.identifier().value(new SimpleAggregateId("id3"));
        runtime.issue(event);
        waitUntilEndOfMessageProcessingOfElapsed(Duration.ofSeconds(3600));
        assertTrue(true);
    }

    private void waitUntilEndOfMessageProcessingOfElapsed(Duration maxWaitTime) {
        for(MessagingConnection connection : runtime.messagingConnections()) {
            @SuppressWarnings("rawtypes")
            MessageReceiver receiver = connection.messageReceiver();
            if(receiver instanceof InternalMessageReceiver) {
                InternalMessageReceiver internalMessageReceiver = (InternalMessageReceiver) receiver;
                internalMessageReceiver.queue().waitUntilEmptyOrInterrupted(Duration.ofSeconds(1), Optional.of(maxWaitTime));
            }
        }
    }
}
