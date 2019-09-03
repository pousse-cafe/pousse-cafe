package poussecafe.domain.chain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import poussecafe.domain.Factory;

public class ChainElementFactory extends Factory<ChainElementId, ChainElement, ChainElement.Attributes> {

    public List<ChainElement> newChain(String chainId, int chainLength) {
        List<ChainElement> chain = new ArrayList<>();
        for(int i = 0; i < chainLength; ++i) {
            ChainElementId currentId = id(chainId, i);
            Optional<ChainElementId> nextId;
            if(i == chainLength - 1) {
                nextId = Optional.empty();
            } else {
                nextId = Optional.of(id(chainId, i + 1));
            }
            ChainElement aggregate = newChainElement(currentId, nextId);
            chain.add(aggregate);
        }
        return chain;
    }

    private ChainElementId id(String chainId, int index) {
        return new ChainElementId(chainId + "_" + Integer.toString(index));
    }

    public ChainElement newChainElement(ChainElementId id,
            Optional<ChainElementId> next) {
        ChainElement aggregate = newAggregateWithId(id);
        aggregate.attributes().touched().value(false);
        aggregate.attributes().next().value(next);
        return aggregate;
    }
}
