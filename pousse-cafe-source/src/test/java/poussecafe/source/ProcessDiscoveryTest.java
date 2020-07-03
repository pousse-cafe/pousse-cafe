package poussecafe.source;

import java.io.IOException;
import java.util.Optional;
import org.junit.Test;
import poussecafe.source.model.MessageListener;

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
        Optional<MessageListener> listener0 = processListener("Process1", "process1Listener0");
        assertTrue(listener0.isPresent());

        Optional<MessageListener> listener1 = processListener("Process1", "process1Listener1");
        assertTrue(listener1.isPresent());

        Optional<MessageListener> listener2 = processListener("Process1", "process1Listener2");
        assertTrue(listener2.isPresent());
    }

    private Optional<MessageListener> processListener(String process, String method) {
        return model().processListeners(process).stream()
                .filter(listener -> listener.methodName().equals(method))
                .findFirst();
    }
}
