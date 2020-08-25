package poussecafe.source;

import java.io.IOException;
import org.junit.Test;

public class MessageListenerDiscoveryTest extends DiscoveryTest {

    @Test
    public void findMessageListeners() throws IOException {
        givenScanner();
        whenIncludingTestModelTree();
        thenAggregateListenersFound();
    }

    private void thenAggregateListenersFound() {
        new ModelAssertions(model()).thenProcess1AggregateListenersFound();
    }
}
