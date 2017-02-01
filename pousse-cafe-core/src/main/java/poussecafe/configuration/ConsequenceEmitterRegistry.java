package poussecafe.configuration;

import java.util.HashMap;
import java.util.Map;
import poussecafe.consequence.ConsequenceEmitter;
import poussecafe.consequence.Source;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.hasKey;
import static poussecafe.check.Predicates.not;

public class ConsequenceEmitterRegistry {

    private Map<Source, ConsequenceEmitter> emitters;

    public ConsequenceEmitterRegistry() {
        emitters = new HashMap<>();
    }

    public void registerEmitter(Source source,
            ConsequenceEmitter emitter) {
        checkThat(value(emitters)
                .verifies(not(hasKey(source)))
                .because("Cannot register several emitters for same source"));
        emitters.put(source, emitter);
    }

    public ConsequenceEmitter getConsequenceEmitter(Source source) {
        return emitters.get(source);
    }
}
