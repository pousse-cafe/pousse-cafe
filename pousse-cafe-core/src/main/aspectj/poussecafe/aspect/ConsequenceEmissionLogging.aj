package poussecafe.aspect;

import org.slf4j.LoggerFactory;
import poussecafe.consequence.Consequence;
import poussecafe.consequence.ConsequenceEmitter;

public aspect ConsequenceEmissionLogging {

    pointcut onConsequenceEmission(Consequence aConsequence) :
        call(void ConsequenceEmitter.emitConsequence(Consequence))
        && args(aConsequence);

    before(Consequence aConsequence) : onConsequenceEmission(aConsequence) {
        ConsequenceEmitter emitter = (ConsequenceEmitter) thisJoinPoint.getTarget();
        LoggerFactory.getLogger(emitter.getClass()).info("Emitting consequence " + aConsequence + " on source " + emitter.getDestinationSource());
    }
}
