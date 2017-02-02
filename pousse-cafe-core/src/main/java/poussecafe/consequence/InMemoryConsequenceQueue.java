package poussecafe.consequence;

import java.util.concurrent.LinkedBlockingQueue;

public class InMemoryConsequenceQueue extends ConsequenceReceiver implements ConsequenceEmitter {

    private LinkedBlockingQueue<Consequence> queue;

    public InMemoryConsequenceQueue(Source source) {
        super(source);
        queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void emitConsequence(Consequence consequence) {
        queue.add(consequence);
    }

    @Override
    public void actuallyStartReceiving() {
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    Consequence consequence = queue.take();
                    onConsequence(consequence);
                }
            } catch (InterruptedException e) {
                return;
            }
        });
        t.setDaemon(true);
        t.setName("in-memory consequence queue");
        t.start();
    }

    @Override
    public Source getDestinationSource() {
        return getSource();
    }

    public void waitUntilEmpty()
            throws InterruptedException {
        while (queue.peek() != null) {
            Thread.sleep(10);
        }
    }

}
