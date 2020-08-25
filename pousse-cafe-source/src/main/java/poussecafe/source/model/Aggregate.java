package poussecafe.source.model;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import poussecafe.source.analysis.ResolvedTypeName;

import static java.util.Objects.requireNonNull;

public class Aggregate {

    private String name;

    public String name() {
        return name;
    }

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

    private String packageName;

    public String packageName() {
        return packageName;
    }

    public static class Builder {

        private Aggregate aggregate = new Aggregate();

        public Aggregate build() {
            requireNonNull(aggregate.name);
            requireNonNull(aggregate.packageName);
            return aggregate;
        }

        public Builder name(ResolvedTypeName name) {
            aggregate.name = name.simpleName();
            aggregate.packageName = name.packageName();
            return this;
        }

        public Optional<String> name() {
            return Optional.ofNullable(aggregate.name);
        }

        public Builder startingFrom(Aggregate other) {
            aggregate.name = other.name;
            aggregate.packageName = other.packageName;
            aggregate.onAddProducedEvents.addAll(other.onAddProducedEvents);
            aggregate.onDeleteProducedEvents.addAll(other.onDeleteProducedEvents);
            return this;
        }

        public Builder onAddProducedEvents(List<ProducedEvent> producedEvents) {
            aggregate.onAddProducedEvents.addAll(producedEvents);
            return this;
        }

        public Builder onAddProducedEvent(ProducedEvent producedEvent) {
            aggregate.onAddProducedEvents.add(producedEvent);
            return this;
        }

        public Builder onDeleteProducedEvents(List<ProducedEvent> onDeleteProducedEvents) {
            aggregate.onDeleteProducedEvents.addAll(onDeleteProducedEvents);
            return this;
        }

        public Builder name(String name) {
            aggregate.name = name;
            return this;
        }

        public Builder packageName(String packageName) {
            aggregate.packageName = packageName;
            return this;
        }
    }

    private Aggregate() {

    }
}
