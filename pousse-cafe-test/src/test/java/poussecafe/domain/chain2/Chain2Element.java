package poussecafe.domain.chain2;

import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.chain1.ChainElementTouchable;
import poussecafe.messaging.internal.ChainElementAttributes;
import poussecafe.messaging.internal.ChainElementTouch;

@Aggregate(
  factory = Chain2ElementFactory.class,
  repository = Chain2ElementRepository.class
)
public class Chain2Element extends AggregateRoot<Chain2ElementId, Chain2Element.Attributes> {

    @MessageListener(runner = Chain2ElementTouchRunner.class)
    public void touch(ChainElementTouchable event) {
        touch.touch();
    }

    private ChainElementTouch<Chain2ElementId, Attributes> touch = new ChainElementTouch<>(this);

    public boolean isTouched() {
        return touch.isTouched();
    }

    public static interface Attributes extends ChainElementAttributes<Chain2ElementId> {

    }
}
