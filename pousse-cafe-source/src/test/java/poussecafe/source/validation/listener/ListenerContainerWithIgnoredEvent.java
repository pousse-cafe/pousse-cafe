package poussecafe.source.validation.listener;

import poussecafe.discovery.Aggregate;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate
public class ListenerContainerWithIgnoredEvent {

    public static class Root extends AggregateRoot<String, Root.Attributes> {

        @ProducesEvent(Message1.class)
        @Override
        public void onAdd() {
        }

        public static interface Attributes extends EntityAttributes<String> {

        }
    }
}
