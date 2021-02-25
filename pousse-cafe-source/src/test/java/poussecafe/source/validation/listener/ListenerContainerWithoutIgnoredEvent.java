package poussecafe.source.validation.listener;

import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate
public class ListenerContainerWithoutIgnoredEvent {

    public static class Root extends AggregateRoot<String, Root.Attributes> {

        @Override
        public void onAdd() {
        }

        public static interface Attributes extends EntityAttributes<String> {

        }
    }
}
