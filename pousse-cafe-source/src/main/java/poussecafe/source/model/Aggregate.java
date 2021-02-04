package poussecafe.source.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import poussecafe.source.Source;

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

    public boolean requiresContainer() {
        return innerFactory
                || innerRoot
                || innerRepository;
    }

    public Optional<Source> containerSource() {
        return containerSource;
    }

    private Optional<Source> containerSource = Optional.empty();

    public Optional<Source> standaloneFactorySource() {
        return standaloneFactorySource;
    }

    private Optional<Source> standaloneFactorySource = Optional.empty();

    public Optional<Source> standaloneRootSource() {
        return standaloneRootSource;
    }

    private Optional<Source> standaloneRootSource = Optional.empty();

    public Optional<Source> standaloneRepositorySource() {
        return standaloneRepositorySource;
    }

    private Optional<Source> standaloneRepositorySource = Optional.empty();

    public static class Builder {

        private Aggregate aggregate = new Aggregate();

        public Aggregate build() {
            requireNonNull(aggregate.name);
            requireNonNull(aggregate.packageName);
            requireNonNull(aggregate.containerSource);
            requireNonNull(aggregate.standaloneFactorySource);
            requireNonNull(aggregate.standaloneRootSource);
            requireNonNull(aggregate.standaloneRepositorySource);

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
            boolean aPrioriInnerFactory = innerFactory.isPresent() && innerFactory.get().booleanValue();
            boolean aPrioriInnerRoot = innerRoot.isPresent() && innerRoot.get().booleanValue();
            boolean aPrioriInnerRepository = innerRepository.isPresent() && innerRepository.get().booleanValue();
            boolean noAPriori = innerFactory.isEmpty()
                    && innerRoot.isEmpty()
                    && !innerRepository.isEmpty();
            if(innerFactory.isEmpty()) {
                innerFactory(noAPriori || (aPrioriInnerRoot || aPrioriInnerRepository));
            }
            if(innerRoot.isEmpty()) {
                innerRoot(noAPriori || (aPrioriInnerFactory || aPrioriInnerRepository));
            }
            if(innerRepository.isEmpty()) {
                innerRepository(noAPriori || (aPrioriInnerFactory || aPrioriInnerRoot));
            }
            return this;
        }

        public Builder containerSource(Optional<Source> containerSource) {
            aggregate.containerSource = containerSource;
            return this;
        }

        public Builder standaloneFactorySource(Optional<Source> standaloneFactorySource) {
            aggregate.standaloneFactorySource = standaloneFactorySource;
            return this;
        }

        public Builder standaloneRootSource(Optional<Source> standaloneRootSource) {
            aggregate.standaloneRootSource = standaloneRootSource;
            return this;
        }

        public Builder standaloneRepositorySource(Optional<Source> standaloneRepositorySource) {
            aggregate.standaloneRepositorySource = standaloneRepositorySource;
            return this;
        }

        public Builder provided(boolean provided) {
            this.provided = provided;
            return this;
        }

        private boolean provided;

        public boolean provided() {
            return provided;
        }
    }

    private Aggregate() {

    }
}
