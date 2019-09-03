package poussecafe.messaging.internal;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.domain.chain.ChainBundle;
import poussecafe.domain.chain.ChainElement;
import poussecafe.domain.chain.ChainElementFactory;
import poussecafe.domain.chain.ChainElementId;
import poussecafe.domain.chain.ChainElementRepository;
import poussecafe.domain.chain.ChainElementTouchable;
import poussecafe.runtime.Runtime.Builder;
import poussecafe.test.PousseCafeTest;

import static org.junit.Assert.assertTrue;

public class WaitOnInternalQueueWorksWithLongChains extends PousseCafeTest {

    @Override
    protected Builder runtimeBuilder() {
        return super.runtimeBuilder()
                .withBundle(ChainBundle.configure().defineAndImplementDefault().build());
    }

    @Test
    public void waitOnInternalQueueWorksWithOneLongChain() {
        givenLongChains(1);
        whenTouchingFirsts();
        thenLastsEventuallyTouched();
    }

    private void givenLongChains(int chains) {
        for(int i = 0; i < chains; ++i) {
            List<ChainElement> chain = factory.newChain(Integer.toString(i), 1000);
            firstIds.add(chain.get(0).attributes().identifier().value());
            lastIds.add(chain.get(chain.size() - 1).attributes().identifier().value());
            chain.stream().forEach(repository::add);
        }
    }

    private List<ChainElementId> firstIds = new ArrayList<>();

    private List<ChainElementId> lastIds = new ArrayList<>();

    private ChainElementFactory factory;

    private ChainElementRepository repository;

    private void whenTouchingFirsts() {
        firstIds.forEach(this::whenTouching);
    }

    private void whenTouching(ChainElementId elementId) {
        ChainElementTouchable event = newDomainEvent(ChainElementTouchable.class);
        event.next().value(elementId);
        emitDomainEvent(event);
    }

    private void thenLastsEventuallyTouched() {
        lastIds.forEach(this::thenElementTouched);
    }

    private void thenElementTouched(ChainElementId elementId) {
        ChainElement aggregate = repository.getOptional(elementId).orElseThrow();
        assertTrue(aggregate.isTouched());
    }

    @Test
    public void waitOnInternalQueueWorksWithMultipleLongChains() {
        givenLongChains(5);
        whenTouchingFirsts();
        thenLastsEventuallyTouched();
    }
}
