package poussecafe.messaging;

import java.io.IOException;
import java.util.Objects;
import poussecafe.exception.PousseCafeException;

public abstract class ReceptionThreadMessageReceiver<E> extends MessageReceiver<E> {

    protected ReceptionThreadMessageReceiver(MessageReceiverConfiguration configuration) {
        super(configuration);

        this.envelopeSource = envelopeSource();
        Objects.requireNonNull(envelopeSource);
    }

    private EnvelopeSource<E> envelopeSource;

    protected abstract EnvelopeSource<E> envelopeSource();

    @Override
    protected void actuallyStartReceiving() {
        receptionThread = new Thread(() -> {
            while (true) {
                E polledObject = null;
                try {
                    polledObject = envelopeSource.get();
                } catch (Exception e) {
                    logger.error("Unable to retrieve next message, closing...", e);
                    break;
                }

                try {
                    onMessage(polledObject);
                } catch (Exception e) {
                    logger.error("Error while handling message ({}), continuing consumption and acking anyway...", e.getMessage());
                    logger.debug("Handling error stacktrace", e);
                    if(polledObject != null) {
                        buildAcker(polledObject).run();
                    }
                }
            }
        });
        receptionThread.setDaemon(true);
        receptionThread.setName("Messaging Reception Thread");
        receptionThread.start();
    }

    private Thread receptionThread;

    @Override
    protected synchronized void actuallyInterruptReception() {
        receptionThread.interrupt();
    }

    @Override
    protected void actuallyStopReceiving() {
        try {
            envelopeSource.close();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to close envelope source", e);
        }
    }

    public void join() {
        if(receptionThread != null) {
            try {
                receptionThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new PousseCafeException("Cloud not join reception thread", e);
            }
        }
    }
}
