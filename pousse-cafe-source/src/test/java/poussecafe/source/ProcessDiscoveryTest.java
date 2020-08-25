package poussecafe.source;

import java.io.IOException;
import org.junit.Test;

public class ProcessDiscoveryTest extends DiscoveryTest {

    @Test
    public void findProcesses() throws IOException { // NOSONAR - assertions in ModelAssertions
        givenScanner();
        whenIncludingTestModelTree();
        thenProcessesFound();
        thenProcessesHaveListeners();
    }

    private void thenProcessesFound() {
        new ModelAssertions(model()).thenProcessesFound();
    }

    private void thenProcessesHaveListeners() {
        new ModelAssertions(model()).thenProcessesHaveListeners();
    }
}
