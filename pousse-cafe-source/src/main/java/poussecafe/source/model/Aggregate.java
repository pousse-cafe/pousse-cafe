package poussecafe.source.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class Aggregate extends ComponentWithType {

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

    public boolean innerFactory() {
        return innerFactory;
    }

    private boolean innerFactory;

    public boolean innerRoot() {
        return innerRoot;
    }

    private boolean innerRoot;

    public boolean innerRepository() {
        return innerRepository;
    }

    private boolean innerRepository;

    public static class Builder {

        private Aggregate aggregate = new Aggregate();

        public Aggregate build() {
            requireNonNull(aggregate.name);
            requireNonNull(aggregate.packageName);

            aggregate.innerFactory = innerFactory.orElseThrow().booleanValue();
            aggregate.innerRoot = innerRoot.orElseThrow().booleanValue();
            aggregate.innerRepository = innerRepository.orElseThrow().booleanValue();

            return aggregate;
        }

        public Optional<String> name() {
            return Optional.ofNullable(aggregate.name);
        }

        public Builder startingFrom(Aggregate other) {
            aggregate.name = other.name;
            aggregate.packageName = other.packageName;
            aggregate.onAddProducedEvents.addAll(other.onAddProducedEvents);
            aggregate.onDeleteProducedEvents.addAll(other.onDeleteProducedEvents);

            innerFactory = Optional.of(other.innerFactory);
            innerRoot = Optional.of(other.innerRoot);
            innerRepository = Optional.of(other.innerRepository);

            return this;
        }

        public Builder onAddProducedEvents(Collection<ProducedEvent> producedEvents) {
            aggregate.onAddProducedEvents.addAll(producedEvents);
            return this;
        }

        public Builder onAddProducedEvent(ProducedEvent producedEvent) {
            aggregate.onAddProducedEvents.add(producedEvent);
            return this;
        }

        public Builder onDeleteProducedEvents(Collection<ProducedEvent> onDeleteProducedEvents) {
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

        public Builder innerFactory(boolean inner) {
            consistentLocationOrElseThrow(innerFactory, inner, "factory");
            innerFactory = Optional.of(inner);
            return this;
        }

        private void consistentLocationOrElseThrow(Optional<Boolean> inner, boolean newValue, String componentName) {
            if(inner.isPresent()
                    && inner.get().booleanValue() != newValue) {
                throw new IllegalArgumentException("Inconsistent " + componentName + " location for aggregate "
                    + aggregate.name + ": must be in a container or not");
            }
        }

        private Optional<Boolean> innerFactory = Optional.empty();

        public Builder innerRoot(boolean inner) {
            consistentLocationOrElseThrow(innerRoot, inner, "root");
            innerRoot = Optional.of(inner);
            return this;
        }

        private Optional<Boolean> innerRoot = Optional.empty();

        public Builder innerRepository(boolean inner) {
            consistentLocationOrElseThrow(innerRepository, inner, "repository");
            innerRepository = Optional.of(inner);
            return this;
        }

        private Optional<Boolean> innerRepository = Optional.empty();

        public Builder ensureDefaultLocations() {
            if(innerFactory.isEmpty()) {
                innerFactory(false);
            }
            if(innerRoot.isEmpty()) {
                innerRoot(false);
            }
            if(innerRepository.isEmpty()) {
                innerRepository(false);
            }
            return this;
        }
    }

    private Aggregate() {

    }
}
