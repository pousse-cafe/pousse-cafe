package poussecafe.consequence;

public class InMemoryConsequenceQueueTest extends ConsequenceReceiverTest {

    @Override
    protected ConsequenceReceiver newConsequenceReceiver(Source source) {
        return new InMemoryConsequenceQueue(source);
    }

}
