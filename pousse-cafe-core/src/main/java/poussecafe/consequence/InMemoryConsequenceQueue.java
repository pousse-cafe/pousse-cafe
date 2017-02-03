package poussecafe.consequence;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class InMemoryConsequenceQueue extends ConsequenceReceiver implements ConsequenceEmitter {

    private Queue<Consequence> queue;

    private Semaphore available = new Semaphore(0);

    private Semaphore mutex = new Semaphore(1);

    public InMemoryConsequenceQueue(Source source) {
        super(source);
        queue = new LinkedList<>();
    }

    @Override
    public void emitConsequence(Consequence consequence) {
        queue.add(consequence);
        available.release();
    }

    @Override
    public void actuallyStartReceiving() {
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    available.acquire();
                    mutex.acquire();
                    Consequence consequence = queue.peek();
                    onConsequence(consequence);
                    queue.poll();
                    mutex.release();
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
        boolean consequencesQueued;
        do {
            mutex.acquire();
            consequencesQueued = !queue.isEmpty();
            mutex.release();
        } while (consequencesQueued);
    }

}
