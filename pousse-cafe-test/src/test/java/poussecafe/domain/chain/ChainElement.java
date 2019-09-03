package poussecafe.domain.chain;

import java.util.Optional;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = ChainElementFactory.class,
  repository = ChainElementRepository.class
)
public class ChainElement extends AggregateRoot<ChainElementId, ChainElement.Attributes> {

    @MessageListener(runner = ChainElementTouchRunner.class)
    public void touch(ChainElementTouchable event) {
        attributes().touched().value(true);

        Optional<ChainElementId> next = attributes().next().value();
        if(next.isPresent()) {
            ChainElementTouchable newEvent = newDomainEvent(ChainElementTouchable.class);
            newEvent.next().value(next.get());
            emitDomainEvent(newEvent);
        }
    }

    public boolean isTouched() {
        return attributes().touched().value();
    }

    public static interface Attributes extends EntityAttributes<ChainElementId> {

        Attribute<Boolean> touched();

        OptionalAttribute<ChainElementId> next();
    }
}
