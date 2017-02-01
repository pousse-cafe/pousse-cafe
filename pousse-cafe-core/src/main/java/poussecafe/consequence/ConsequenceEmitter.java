package poussecafe.consequence;

public interface ConsequenceEmitter {

    void emitConsequence(Consequence consequence);

    Source getDestinationSource();

}
