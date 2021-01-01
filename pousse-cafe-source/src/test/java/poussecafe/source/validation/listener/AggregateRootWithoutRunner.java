package poussecafe.source.validation.listener;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

public class AggregateRootWithoutRunner extends AggregateRoot<String, AggregateRootWithoutRunner.Attributes> {

    @MessageListener
    public void updator(Message2 message) {

    }

    public static interface Attributes extends EntityAttributes<String> {

    }
}
