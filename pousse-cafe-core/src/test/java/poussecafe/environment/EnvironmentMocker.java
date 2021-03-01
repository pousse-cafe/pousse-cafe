package poussecafe.environment;

import java.util.Optional;
import org.mockito.Mockito;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;

import static java.util.Objects.requireNonNull;
import static org.mockito.Mockito.when;

public class EnvironmentMocker {

    private Environment environment;

    public Environment mock() {
        return environment;
    }

    @SuppressWarnings("rawtypes")
    public static class Builder {

        public EnvironmentMocker build() {
            AggregateServices services = null;
            if(rootClass != null) {
                services = Mockito.mock(AggregateServices.class);
                when(services.aggregateRootEntityClass()).thenReturn(rootClass);
                when(mocker.environment.aggregateServicesOf(rootClass)).thenReturn(Optional.of(services));
            }

            if(repositoryClass != null) {
                requireNonNull(rootClass);

                AggregateRepository repository = Mockito.mock(repositoryClass);
                when(repository.entityClass()).thenReturn(rootClass);

                when(services.repository()).thenReturn(repository);
                when(mocker.environment.repository(repositoryClass)).thenReturn(Optional.of(repository));
            }

            return mocker;
        }

        private EnvironmentMocker mocker = new EnvironmentMocker();

        public Builder aggregateRootClass(Class<? extends AggregateRoot> rootClass) {
            this.rootClass = rootClass;
            return this;
        }

        private Class<? extends AggregateRoot> rootClass;

        public Builder repositoryClass(Class<? extends AggregateRepository> repositoryClass) {
            this.repositoryClass = repositoryClass;
            return this;
        }

        private Class<? extends AggregateRepository> repositoryClass;
    }

    private EnvironmentMocker() {
        environment = Mockito.mock(Environment.class);
    }
}
