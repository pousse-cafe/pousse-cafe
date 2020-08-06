package poussecafe.environment;

import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.discovery.DefaultModule;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Module;
import poussecafe.util.Equality;

@SuppressWarnings("rawtypes")
public class AggregateDefinition {

    public Class<? extends AggregateRoot> getAggregateRootClass() {
        return aggregateRootClass;
    }

    private Class<? extends AggregateRoot> aggregateRootClass;

    public Optional<Class<? extends Module>> moduleClass() {
        return moduleClass;
    }

    private Optional<Class<? extends Module>> moduleClass = Optional.empty();

    public boolean hasFactory() {
        return factoryClass != null;
    }

    public Class getFactoryClass() {
        return factoryClass;
    }

    private Class<?> factoryClass;

    public Class getRepositoryClass() {
        return repositoryClass;
    }

    private Class<?> repositoryClass;

    public boolean hasRepository() {
        return repositoryClass != null;
    }

    public String getSimpleName() {
        return NamingConvention.simpleAggregateName(aggregateRootClass);
    }

    public String getQualifiedName() {
        return NamingConvention.qualifiedAggregateName(moduleClass.orElse(DefaultModule.class), aggregateRootClass);
    }

    public static class Builder {

        public Builder() {
            definition = new AggregateDefinition();
        }

        private AggregateDefinition definition;

        public Builder withAggregateRoot(Class<? extends AggregateRoot> aggregateRootClass) {
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

        public Builder withModuleClass(Optional<Class<? extends Module>> moduleClass) {
            definition.moduleClass = moduleClass;
            return this;
        }

        public AggregateDefinition build() {
            Objects.requireNonNull(definition.aggregateRootClass);
            Objects.requireNonNull(definition.factoryClass);
            Objects.requireNonNull(definition.repositoryClass);
            Objects.requireNonNull(definition.moduleClass);
            return definition;
        }
    }

    private AggregateDefinition() {

    }

    @Override
    public boolean equals(Object obj) {
        return Equality.referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(aggregateRootClass, other.aggregateRootClass)
                .append(factoryClass, other.factoryClass)
                .append(repositoryClass, other.repositoryClass)
                .append(moduleClass, other.moduleClass)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(aggregateRootClass)
                .append(factoryClass)
                .append(repositoryClass)
                .append(moduleClass)
                .build();
    }
}
