package poussecafe.source;

import java.io.IOException;
import java.util.Optional;
import org.junit.Test;
import poussecafe.source.model.MessageListenerSource;

import static org.junit.Assert.assertTrue;

public class ProcessDiscoveryTest extends DiscoveryTest {

    @Test
    public void findProcesses() throws IOException {
        givenScanner();
        whenIncludingTestModelTree();
        thenProcessesFound();
        thenProcessesHaveListeners();
    }

    private void thenProcessesFound() {
        assertTrue(model().process("Process1").isPresent());
    }

    private void thenProcessesHaveListeners() {
        Optional<MessageListenerSource> listener1 = processListener("Process1", "Aggregate1", "process1Listener1", "Event1");
        assertTrue(listener1.isPresent());

        Optional<MessageListenerSource> listener2 = processListener("Process1", "Aggregate2", "process1Listener2", "Event2");
        assertTrue(listener2.isPresent());
    }

    private Optional<MessageListenerSource> processListener(String process, String aggregate, String method, String message) {
        return model().processListeners(process).stream()
                .filter(listener -> listener.container().aggregateName().isPresent())
                .filter(listener -> listener.container().aggregateName().get().equals(aggregate))
                .filter(listener -> listener.methodName().equals(method))
                .filter(listener -> listener.messageName().equals(message))
                .findFirst();
    }
}
