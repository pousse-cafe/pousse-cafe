package poussecafe.domain.chain1;

import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRoot;
import poussecafe.messaging.internal.ChainElementAttributes;
import poussecafe.messaging.internal.ChainElementTouch;

@Aggregate(
  factory = Chain1ElementFactory.class,
  repository = Chain1ElementRepository.class
)
public class Chain1Element extends AggregateRoot<Chain1ElementId, Chain1Element.Attributes> {

    @MessageListener(runner = Chain1ElementTouchRunner.class)
    public void touch(ChainElementTouchable event) {
        touch.touch();
    }

    private ChainElementTouch<Chain1ElementId, Attributes> touch = new ChainElementTouch<>(this);

    public boolean isTouched() {
        return touch.isTouched();
    }

    public static interface Attributes extends ChainElementAttributes<Chain1ElementId> {

    }
}
