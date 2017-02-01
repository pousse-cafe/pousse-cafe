package poussecafe.consequence;

import poussecafe.configuration.ConsequenceEmitterRegistry;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class ConsequenceRouter {

    private SourceSelector selector;

    private ConsequenceEmitterRegistry registry;

    public void routeConsequence(Consequence consequence) {
        Source source = selector.selectSource(consequence);
        ConsequenceEmitter emitter = registry.getConsequenceEmitter(source);
        emitter.emitConsequence(consequence);
    }

    public void setSourceSelector(SourceSelector selector) {
        checkThat(value(selector).notNull().because("Source selector cannot be null"));
        this.selector = selector;
    }

    public void setConsequenceEmitterRegistry(ConsequenceEmitterRegistry registry) {
        checkThat(value(registry).notNull().because("Consequence emitter registry cannot be null"));
        this.registry = registry;
    }
}
