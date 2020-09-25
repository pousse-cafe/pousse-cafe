package poussecafe.messaging.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Factory;
import poussecafe.test.domain.chain1.Chain1ElementId;
import poussecafe.test.domain.chain2.Chain2ElementId;
import poussecafe.test.domain.chain3.Chain3ElementId;
import poussecafe.util.StringId;

public abstract class ChainElementFactory<I, D extends ChainElementAttributes<I>, A extends AggregateRoot<I, D>>
extends Factory<I, A, D> {

    protected ChainElementFactory(int typeNumber) {
        this.typeNumber = typeNumber;
    }

    private int typeNumber;

    private static final int TYPES = 3;

    public List<A> newChain(String chainId, int chainLength) {
        return newChain(chainId, chainLength, false);
    }

    private List<A> newChain(String chainId, int chainLength, boolean interleaved) {
        List<A> chain = new ArrayList<>();
        for(int i = 0; i < chainLength; ++i) {
            @SuppressWarnings("unchecked")
            I currentId = (I) newId(typeNumber, chainId, i);
            Optional<NextChainElementId> next;
            if(i == chainLength - 1) {
                next = Optional.empty();
            } else {
                if(interleaved) {
                    int nextTypeNumber = ((typeNumber + 1) % TYPES) + 1;
                    StringId nextId = newId(nextTypeNumber, chainId, i + 1);
                    next = Optional.of(new NextChainElementId(nextTypeNumber, nextId.stringValue()));
                } else {
                    StringId nextId = newId(typeNumber, chainId, i + 1);
                    next = Optional.of(new NextChainElementId(typeNumber, nextId.stringValue()));
                }
            }
            A aggregate = newChainElement(currentId, next);
            chain.add(aggregate);
        }
        return chain;
    }

    protected StringId newId(int typeNumber, String chainId, int index) {
        if(typeNumber == 1) {
            return new Chain1ElementId(chainId, index);
        } else if(typeNumber == 2) {
            return new Chain2ElementId(chainId, index);
        } else if(typeNumber == 3) {
            return new Chain3ElementId(chainId, index);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public A newChainElement(I id,
            Optional<NextChainElementId> next) {
        A aggregate = newAggregateWithId(id);
        aggregate.attributes().touched().value(false);
        aggregate.attributes().next().value(next);
        return aggregate;
    }

    public List<A> newInterleavedChain(String chainId, int chainLength) {
        return newChain(chainId, chainLength, true);
    }
}
