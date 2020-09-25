package poussecafe.test.domain.chain3;

import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRoot;
import poussecafe.messaging.internal.ChainElementAttributes;
import poussecafe.messaging.internal.ChainElementTouch;
import poussecafe.test.domain.chain1.ChainElementTouchable;

@Aggregate(
  factory = Chain3ElementFactory.class,
  repository = Chain3ElementRepository.class
)
public class Chain3Element extends AggregateRoot<Chain3ElementId, Chain3Element.Attributes> {

    @MessageListener(runner = Chain3ElementTouchRunner.class)
    public void touch(ChainElementTouchable event) {
        touch.touch();
    }

    private ChainElementTouch<Chain3ElementId, Attributes> touch = new ChainElementTouch<>(this);

    public boolean isTouched() {
        return touch.isTouched();
    }

    public static interface Attributes extends ChainElementAttributes<Chain3ElementId> {

    }
}
