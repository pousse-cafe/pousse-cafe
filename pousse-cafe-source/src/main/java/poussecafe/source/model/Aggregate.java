package poussecafe.source.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import poussecafe.source.Source;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("serial")
public class Aggregate extends ComponentWithType implements Serializable {

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
        return Optional.ofNullable(containerSource);
    }

    private Source containerSource;

    public Optional<Source> standaloneFactorySource() {
        return Optional.ofNullable(standaloneFactorySource);
    }

    private Source standaloneFactorySource;

    public Optional<Source> standaloneRootSource() {
        return Optional.ofNullable(standaloneRootSource);
    }

    private Source standaloneRootSource;

    public Optional<Source> standaloneRepositorySource() {
        return Optional.ofNullable(standaloneRepositorySource);
    }

    private Source standaloneRepositorySource;

    public static class Builder implements Serializable {

        private Aggregate aggregate = new Aggregate();

        public Aggregate build() {
            requireNonNull(aggregate.name);
            requireNonNull(aggregate.packageName);

            aggregate.innerFactory = innerFactory.booleanValue();
            aggregate.innerRoot = innerRoot.booleanValue();
            aggregate.innerRepository = innerRepository.booleanValue();

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

            innerFactory = other.innerFactory;
            innerRoot = other.innerRoot;
            innerRepository = other.innerRepository;

            aggregate.containerSource = other.containerSource;
            aggregate.standaloneFactorySource = other.standaloneFactorySource;
            aggregate.standaloneRootSource = other.standaloneRootSource;
            aggregate.standaloneRepositorySource = other.standaloneRepositorySource;

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

        public Builder onDeleteProducedEvent(ProducedEvent producedEvent) {
            aggregate.onDeleteProducedEvents.add(producedEvent);
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
            innerFactory = inner;
            return this;
        }

        private void consistentLocationOrElseThrow(Boolean inner, boolean newValue, String componentName) {
            if(inner != null
                    && inner.booleanValue() != newValue) {
                throw new IllegalArgumentException("Inconsistent " + componentName + " location for aggregate "
                    + aggregate.name + ": must be in a container or not");
            }
        }

        private Boolean innerFactory;

        public Builder innerRoot(boolean inner) {
            consistentLocationOrElseThrow(innerRoot, inner, "root");
            innerRoot = inner;
            return this;
        }

        private Boolean innerRoot;

        public Builder innerRepository(boolean inner) {
            consistentLocationOrElseThrow(innerRepository, inner, "repository");
            innerRepository = inner;
            return this;
        }

        private Boolean innerRepository;

        public Builder ensureDefaultLocations() {
            boolean aPrioriInnerFactory = innerFactory != null && innerFactory.booleanValue();
            boolean aPrioriInnerRoot = innerRoot != null && innerRoot.booleanValue();
            boolean aPrioriInnerRepository = innerRepository != null && innerRepository.booleanValue();
            boolean noAPriori = innerFactory == null
                    && innerRoot == null
                    && innerRepository == null;
            if(innerFactory == null) {
                innerFactory(noAPriori || (aPrioriInnerRoot || aPrioriInnerRepository));
            }
            if(innerRoot == null) {
                innerRoot(noAPriori || (aPrioriInnerFactory || aPrioriInnerRepository));
            }
            if(innerRepository == null) {
                innerRepository(noAPriori || (aPrioriInnerFactory || aPrioriInnerRoot));
            }
            return this;
        }

        public Builder containerSource(Optional<Source> containerSource) {
            aggregate.containerSource = containerSource.orElse(null);
            return this;
        }

        public Builder standaloneFactorySource(Optional<Source> standaloneFactorySource) {
            aggregate.standaloneFactorySource = standaloneFactorySource.orElse(null);
            return this;
        }

        public Builder standaloneRootSource(Optional<Source> standaloneRootSource) {
            aggregate.standaloneRootSource = standaloneRootSource.orElse(null);
            return this;
        }

        public Builder standaloneRepositorySource(Optional<Source> standaloneRepositorySource) {
            aggregate.standaloneRepositorySource = standaloneRepositorySource.orElse(null);
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
