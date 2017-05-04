package poussecafe.configuration;

import java.util.HashMap;
import java.util.Map;
import poussecafe.messaging.MessageSender;
import poussecafe.messaging.Queue;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.hasKey;
import static poussecafe.check.Predicates.not;

public class MessageSenderRegistry {

    private Map<Queue, MessageSender> emitters;

    public MessageSenderRegistry() {
        emitters = new HashMap<>();
    }

    public void registerEmitter(Queue source,
            MessageSender emitter) {
        checkThat(value(emitters)
                .verifies(not(hasKey(source)))
                .because("Cannot register several senders for same source"));
        emitters.put(source, emitter);
    }

    public MessageSender getMessageSender(Queue destination) {
        return emitters.get(destination);
    }
}
