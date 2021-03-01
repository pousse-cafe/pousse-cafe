package poussecafe.environment;

import java.lang.reflect.Method;
import java.util.Optional;
import org.junit.Before;
import poussecafe.environment.RepositoryListenerTestComponents.Event;
import poussecafe.environment.RepositoryListenerTestComponents.Repository;
import poussecafe.environment.RepositoryListenerTestComponents.Root;
import poussecafe.util.MethodInvoker;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class DeleteAggregatesMessageConsumerTest {

    @Before
    public void givenStateAndEnvironment() {
        state = MessageListenerGroupConsumptionStateMock.mock(Event.class);
        environmentMocker = new EnvironmentMocker.Builder()
                .aggregateRootClass(Root.class)
                .repositoryClass(Repository.class)
                .build();
    }

    private MessageListenerGroupConsumptionState state;

    private EnvironmentMocker environmentMocker;

    public void givenExistingAggregate() {
        var repository = repository();
        existingAggregate = mock(Root.class);
        when(repository.getOptional(IDENTIFIER)).thenReturn(Optional.of(existingAggregate));
    }

    public static final String IDENTIFIER = "id";

    private Root existingAggregate;

    public Repository repository() {
        return (Repository) environmentMocker.mock().repository(Repository.class).orElseThrow();
    }

    public void givenInvoker(Method method) {
        invoker = new MethodInvoker.Builder()
                .method(method)
                .target(repository())
                .build();
    }

    private MethodInvoker invoker;

    public MethodInvoker invoker() {
        return invoker;
    }

    public void whenConsumingState() {
        consumer().consume(state);
    }

    protected abstract MessageConsumer consumer();

    public void thenRepositoryDelete() {
        var repository = repository();
        verify(repository).delete(existingAggregate);
    }

    public Environment environment() {
        return environmentMocker.mock();
    }
}
