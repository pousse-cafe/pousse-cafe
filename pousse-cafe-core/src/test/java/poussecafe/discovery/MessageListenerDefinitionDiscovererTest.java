package poussecafe.discovery;

import java.util.Collection;
import java.util.List;
import org.junit.Test;
import poussecafe.environment.ExpectedEvent;
import poussecafe.environment.MessageListenerDefinition;
import poussecafe.testmodule.TestDomainEvent2;
import poussecafe.testmodule.TestDomainEvent3;

import static org.junit.Assert.assertTrue;

public class MessageListenerDefinitionDiscovererTest {

    @Test
    public void aggregateRootWithListenerProducesEventHasExpectedEvents() {
        givenAggregateRootWithSingleListener();
        whenDiscoveringListenerDefinitions();
        thenSingleListenerDefinitionHasExpectedEvents();
    }

    private void givenAggregateRootWithSingleListener() {
        containerClass = AggregateRootWithSingleListener.class;
    }

    @SuppressWarnings("rawtypes")
    private Class containerClass;

    private void whenDiscoveringListenerDefinitions() {
        definitions = new MessageListenerDefinitionDiscoverer(containerClass).discoverListenersOfClass();
    }

    private Collection<MessageListenerDefinition> definitions;

    private void thenSingleListenerDefinitionHasExpectedEvents() {
        MessageListenerDefinition definition = definitions.iterator().next();
        assertTrue(definition.withExpectedEvents());
        List<ExpectedEvent> expectedEvents = definition.expectedEvents();
        assertTrue(expectedEvents.contains(new ExpectedEvent.Builder()
                .producedEventClass(TestDomainEvent2.class)
                .required(false)
                .build()));
        assertTrue(expectedEvents.contains(new ExpectedEvent.Builder()
                .producedEventClass(TestDomainEvent3.class)
                .required(true)
                .build()));
    }

    @Test
    public void factoryWithListenerProducesEventHasExpectedEvents() {
        givenFactoryWithSingleListener();
        whenDiscoveringListenerDefinitions();
        thenSingleListenerDefinitionHasExpectedEvents();
    }

    private void givenFactoryWithSingleListener() {
        containerClass = FactoryWithSingleListener.class;
    }

    @Test
    public void repositoryWithListenerProducesEventHasExpectedEvents() {
        givenRepositoryWithSingleListener();
        whenDiscoveringListenerDefinitions();
        thenSingleListenerDefinitionHasExpectedEvents();
    }

    private void givenRepositoryWithSingleListener() {
        containerClass = RepositoryWithSingleListener.class;
    }
}
