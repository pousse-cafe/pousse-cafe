package poussecafe.messaging.internal;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import poussecafe.domain.AggregateRoot;
import poussecafe.test.domain.chain1.ChainElementTouchable;

public class ChainElementTouch<I, D extends ChainElementAttributes<I>> {

    public ChainElementTouch(AggregateRoot<I, D> chainElement) {
        Objects.requireNonNull(chainElement);
        this.chainElement = chainElement;
    }

    private AggregateRoot<I, D> chainElement;

    public void touch() {
        attributes().touched().value(true);

        int iterations = RANDOM.nextInt(100);
        for(int i = 0; i < iterations; ++i) {
            Integer.toString(i);
        }

        Optional<NextChainElementId> optionalNext = attributes().next().value();
        if(optionalNext.isPresent()) {
            NextChainElementId next = optionalNext.get();
            ChainElementTouchable newEvent = chainElement.newDomainEvent(ChainElementTouchable.class);
            newEvent.next().value(next);
            chainElement.issue(newEvent);
        }
    }

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private ChainElementAttributes<I> attributes() {
        return chainElement.attributes();
    }

    public boolean isTouched() {
        return attributes().touched().value();
    }
}
