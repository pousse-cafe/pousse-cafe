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
        Optional<MessageListenerSource> listener1 = processListener("Process1", "process1Listener1");
        assertTrue(listener1.isPresent());

        Optional<MessageListenerSource> listener2 = processListener("Process1", "process1Listener2");
        assertTrue(listener2.isPresent());
    }

    private Optional<MessageListenerSource> processListener(String process, String method) {
        return model().processListeners(process).stream()
                .filter(listener -> listener.methodName().equals(method))
                .findFirst();
    }
}
