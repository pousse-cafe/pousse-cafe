package poussecafe.source;

import java.io.IOException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ProcessDiscoveryTest extends DiscoveryTest {

    @Test
    public void findProcesses() throws IOException {
        givenScanner();
        whenIncludingTestModelTree();
        thenProcessesFound();
    }

    private void thenProcessesFound() {
        assertTrue(model().process("Process1").isPresent());
    }
}
