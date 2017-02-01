package poussecafe.consequence;

import poussecafe.journal.ConsequenceJournal;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class ConsequenceReceiver {

    private Source consequenceSource;

    private ConsequenceListenerRegistry listenerRegistry;

    private ConsequenceJournal consequenceJournal;

    private boolean started;

    protected ConsequenceReceiver(Source consequenceSource) {
        setConsequenceSource(consequenceSource);
    }

    private void setConsequenceSource(Source consequenceSource) {
        checkThat(value(consequenceSource).notNull().because("Consequence source cannot be null"));
        this.consequenceSource = consequenceSource;
    }

    protected void onConsequence(Consequence receivedConsequence) {
        checkThat(value(receivedConsequence).notNull().because("Received consequence cannot be null"));
        for (ConsequenceListener listener : listenerRegistry
                .getListeners(new ConsequenceListenerRoutingKey(consequenceSource, receivedConsequence.getClass()))) {
            if (!consequenceJournal.isSuccessfullyConsumed(receivedConsequence, listener.getListenerId())) {
                consumeConsequence(receivedConsequence, listener);
            } else {
                ignoreConsequence(receivedConsequence, listener);
            }
        }
    }

    private void consumeConsequence(Consequence receivedConsequence,
            ConsequenceListener listener) {
        try {
            listener.consume(receivedConsequence);
            consequenceJournal.logSuccessfulConsumption(listener.getListenerId(), receivedConsequence);
        } catch (Exception e) {
            consequenceJournal.logFailedConsumption(listener.getListenerId(), receivedConsequence, e);
        }
    }

    private void ignoreConsequence(Consequence receivedConsequence,
            ConsequenceListener listener) {
        consequenceJournal.logIgnoredConsumption(listener.getListenerId(), receivedConsequence);
    }

    public void startReceiving() {
        if (started) {
            return;
        }
        actuallyStartReceiving();
        started = true;
    }

    protected abstract void actuallyStartReceiving();

    public Source getSource() {
        return consequenceSource;
    }

    public void setListenerRegistry(ConsequenceListenerRegistry listenerRegistry) {
        checkThat(value(listenerRegistry).notNull().because("Consequence listener registry cannot be null"));
        this.listenerRegistry = listenerRegistry;
    }

    public void setConsequenceJournal(ConsequenceJournal consequenceJournal) {
        checkThat(value(consequenceJournal).notNull().because("Consequence journal cannot be null"));
        this.consequenceJournal = consequenceJournal;
    }

    public boolean isStarted() {
        return started;
    }
}
