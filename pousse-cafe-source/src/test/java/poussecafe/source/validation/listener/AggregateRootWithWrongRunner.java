package poussecafe.source.validation.listener;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

public class AggregateRootWithWrongRunner extends AggregateRoot<String, AggregateRootWithWrongRunner.Attributes> {

    @MessageListener(runner = WrongRunner.class)
    public void updator(Message2 message) {

    }

    public static interface Attributes extends EntityAttributes<String> {

    }
}
