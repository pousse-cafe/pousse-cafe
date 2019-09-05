package poussecafe.messaging.internal;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.domain.chain1.Chain1Bundle;
import poussecafe.domain.chain1.Chain1Element;
import poussecafe.domain.chain1.Chain1ElementFactory;
import poussecafe.domain.chain1.Chain1ElementRepository;
import poussecafe.domain.chain1.ChainElementTouchable;
import poussecafe.domain.chain2.Chain2Bundle;
import poussecafe.domain.chain2.Chain2Element;
import poussecafe.domain.chain2.Chain2ElementFactory;
import poussecafe.domain.chain2.Chain2ElementRepository;
import poussecafe.domain.chain3.Chain3Bundle;
import poussecafe.domain.chain3.Chain3Element;
import poussecafe.domain.chain3.Chain3ElementFactory;
import poussecafe.domain.chain3.Chain3ElementRepository;
import poussecafe.runtime.Runtime.Builder;
import poussecafe.test.PousseCafeTest;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertTrue;

public class WaitOnInternalQueueWorksWithLongChains extends PousseCafeTest {

    @Override
    protected Builder runtimeBuilder() {
        return super.runtimeBuilder()
                .processingThreads(2)
                .withBundle(Chain1Bundle.configure().defineAndImplementDefault().build())
                .withBundle(Chain2Bundle.configure().defineAndImplementDefault().build())
                .withBundle(Chain3Bundle.configure().defineAndImplementDefault().build());
    }

    @Test
    public void waitOnInternalQueueWorksWithOneLongChain() {
        givenLongChains(1, 1);
        whenTouchingFirstsOfChains();
        thenLastsOfChainsEventuallyTouched();
    }

    private void givenLongChains(int typeNumber, int chains) {
        for(int i = 0; i < chains; ++i) {
            if(typeNumber == 1) {
                List<Chain1Element> chain = chain1ElementFactory.newChain(Integer.toString(i), chainLength);
                queueChain1(chain);
            } else if(typeNumber == 2) {
                List<Chain2Element> chain = chain2ElementFactory.newChain(Integer.toString(i), chainLength);
                queueChain2(chain);
            } else if(typeNumber == 3) {
                List<Chain3Element> chain = chain3ElementFactory.newChain(Integer.toString(i), chainLength);
                queueChain3(chain);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private void queueChain1(List<Chain1Element> chain) {
        firstIdsChain.add(new NextChainElementId(chain.get(0).attributes().identifier().value()));
        lastIdsChain.add(new NextChainElementId(chain.get(chain.size() - 1).attributes().identifier().value()));
        chain.stream().forEach(chain1ElementRepository::add);
    }

    private void queueChain2(List<Chain2Element> chain) {
        firstIdsChain.add(new NextChainElementId(chain.get(0).attributes().identifier().value()));
        lastIdsChain.add(new NextChainElementId(chain.get(chain.size() - 1).attributes().identifier().value()));
        chain.stream().forEach(chain2ElementRepository::add);
    }

    private void queueChain3(List<Chain3Element> chain) {
        firstIdsChain.add(new NextChainElementId(chain.get(0).attributes().identifier().value()));
        lastIdsChain.add(new NextChainElementId(chain.get(chain.size() - 1).attributes().identifier().value()));
        chain.stream().forEach(chain3ElementRepository::add);
    }

    private List<NextChainElementId> firstIdsChain = new ArrayList<>();

    private List<NextChainElementId> lastIdsChain = new ArrayList<>();

    private Chain1ElementFactory chain1ElementFactory;

    private int chainLength = 50;

    private Chain1ElementRepository chain1ElementRepository;

    private Chain2ElementFactory chain2ElementFactory;

    private Chain2ElementRepository chain2ElementRepository;

    private Chain3ElementFactory chain3ElementFactory;

    private Chain3ElementRepository chain3ElementRepository;

    private void whenTouchingFirstsOfChains() {
        List<ChainElementTouchable> elements = firstIdsChain.stream().map(this::map).collect(toList());
        emitDomainEvents(elements);
    }

    private ChainElementTouchable map(NextChainElementId elementId) {
        ChainElementTouchable event = newDomainEvent(ChainElementTouchable.class);
        event.next().value(elementId);
        return event;
    }

    private void thenLastsOfChainsEventuallyTouched() {
        lastIdsChain.forEach(this::thenChain1ElementTouched);
    }

    private void thenChain1ElementTouched(NextChainElementId elementId) {
        int typeNumber = elementId.typeNumber();
        if(typeNumber == 1) {
            Chain1Element aggregate = chain1ElementRepository.getOptional(elementId.toChain1ElementId()).orElseThrow();
            assertTrue(aggregate.isTouched());
        } else if(typeNumber == 2) {
            Chain2Element aggregate = chain2ElementRepository.getOptional(elementId.toChain2ElementId()).orElseThrow();
            assertTrue(aggregate.isTouched());
        } else if(typeNumber == 3) {
            Chain3Element aggregate = chain3ElementRepository.getOptional(elementId.toChain3ElementId()).orElseThrow();
            assertTrue(aggregate.isTouched());
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Test
    public void waitOnInternalQueueWorksWithMultipleLongChains() {
        givenLongChains(1, 5);
        whenTouchingFirstsOfChains();
        thenLastsOfChainsEventuallyTouched();
    }

    @Test
    public void waitOnInternalQueueWorksWithMultipleDifferentLongChains() {
        givenDifferentLongChains(5);
        whenTouchingFirstsOfChains();
        thenLastsOfChainsEventuallyTouched();
    }

    private void givenDifferentLongChains(int chainsPerType) {
        givenLongChains(1, chainsPerType);
        givenLongChains(2, chainsPerType);
        givenLongChains(3, chainsPerType);
    }

    @Test
    public void waitOnInternalQueueWorksWithMultipleDifferentInterleavedLongChains() {
        givenDifferentInterleavedLongChains(10);
        whenTouchingFirstsOfChains();
        thenLastsOfChainsEventuallyTouched();
    }

    private void givenDifferentInterleavedLongChains(int chainsPerType) {
        givenLongInterleavedChains(1, chainsPerType);
        givenLongInterleavedChains(2, chainsPerType);
        givenLongInterleavedChains(3, chainsPerType);
    }

    private void givenLongInterleavedChains(int typeNumber, int chains) {
        for(int i = 0; i < chains; ++i) {
            if(typeNumber == 1) {
                List<Chain1Element> chain = chain1ElementFactory.newInterleavedChain(Integer.toString(i), chainLength);
                queueChain1(chain);
            } else if(typeNumber == 2) {
                List<Chain2Element> chain = chain2ElementFactory.newInterleavedChain(Integer.toString(i), chainLength);
                queueChain2(chain);
            } else if(typeNumber == 3) {
                List<Chain3Element> chain = chain3ElementFactory.newInterleavedChain(Integer.toString(i), chainLength);
                queueChain3(chain);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}
