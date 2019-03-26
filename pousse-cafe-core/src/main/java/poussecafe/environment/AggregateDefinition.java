package poussecafe.environment;

import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.util.ReferenceEquals;

public class AggregateDefinition {

    public Class<?> getAggregateRootClass() {
        return aggregateRootClass;
    }

    private Class<?> aggregateRootClass;

    public boolean hasFactory() {
        return factoryClass != null;
    }

    @SuppressWarnings("rawtypes")
    public Class getFactoryClass() {
        return factoryClass;
    }

    private Class<?> factoryClass;

    @SuppressWarnings("rawtypes")
    public Class getRepositoryClass() {
        return repositoryClass;
    }

    private Class<?> repositoryClass;

    public boolean hasRepository() {
        return repositoryClass != null;
    }

    public static class Builder {

        public Builder() {
            definition = new AggregateDefinition();
        }

        private AggregateDefinition definition;

        public Builder withAggregateRoot(Class<?> aggregateRootClass) {
            definition.aggregateRootClass = aggregateRootClass;
            return this;
        }

        public Builder withFactoryClass(Class<?> factoryClass) {
            definition.factoryClass = factoryClass;
            return this;
        }

        public Builder withRepositoryClass(Class<?> repositoryClass) {
            definition.repositoryClass = repositoryClass;
            return this;
        }

        public AggregateDefinition build() {
            Objects.requireNonNull(definition.aggregateRootClass);
            Objects.requireNonNull(definition.factoryClass);
            Objects.requireNonNull(definition.repositoryClass);
            return definition;
        }
    }

    private AggregateDefinition() {

    }

    @Override
    public boolean equals(Object obj) {
        return ReferenceEquals.referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(aggregateRootClass, other.aggregateRootClass)
                .append(factoryClass, other.factoryClass)
                .append(repositoryClass, other.repositoryClass)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(aggregateRootClass)
                .append(factoryClass)
                .append(repositoryClass)
                .build();
    }
}
