package poussecafe.source.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class Hooks implements Serializable {

    public static final String ON_ADD_METHOD_NAME = "onAdd";

    public Set<ProducedEvent> onAddProducedEvents() {
        return onAddProducedEvents;
    }

    private Set<ProducedEvent> onAddProducedEvents = new HashSet<>();

    public static final String ON_DELETE_METHOD_NAME = "onDelete";

    public Set<ProducedEvent> onDeleteProducedEvents() {
        return onDeleteProducedEvents;
    }

    private Set<ProducedEvent> onDeleteProducedEvents = new HashSet<>();

    public static class Builder {

        public Hooks build() {
            return hooks;
        }

        private Hooks hooks = new Hooks();

        public Builder onAddProducedEvents(Collection<ProducedEvent> producedEvents) {
            hooks.onAddProducedEvents.addAll(producedEvents);
            return this;
        }

        public Builder onAddProducedEvent(ProducedEvent producedEvent) {
            hooks.onAddProducedEvents.add(producedEvent);
            return this;
        }

        public Builder onDeleteProducedEvents(Collection<ProducedEvent> onDeleteProducedEvents) {
            hooks.onDeleteProducedEvents.addAll(onDeleteProducedEvents);
            return this;
        }

        public Builder onDeleteProducedEvent(ProducedEvent onDeleteProducedEvent) {
            hooks.onDeleteProducedEvents.add(onDeleteProducedEvent);
            return this;
        }
    }

    private Hooks() {

    }

    public static final Hooks EMPTY = new Hooks.Builder().build();

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(onAddProducedEvents, other.onAddProducedEvents)
                .append(onDeleteProducedEvents, other.onDeleteProducedEvents)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(onAddProducedEvents)
                .append(onDeleteProducedEvents)
                .build();
    }
}
