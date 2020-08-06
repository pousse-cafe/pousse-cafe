package poussecafe.processing;

import org.junit.Test;
import poussecafe.testmodule.CreateSimpleAggregate;
import poussecafe.testmodule.SimpleAggregateId;
import poussecafe.testmodule.SimpleAggregateRepository;
import poussecafe.testmodule.TestDomainEvent5;
import poussecafe.testmodule.TestModuleTestCase;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class AlternativeIdentifierTest extends TestModuleTestCase {

    @Test
    public void updateWithIdentifierHandler() {
        givenAggregate();
        whenUpdating();
        thenAggregateTouched();
    }

    private void givenAggregate() {
        CreateSimpleAggregate command = runtime().newCommand(CreateSimpleAggregate.class);
        command.identifier().value(id);
        command.data().value(data);
        runtime().submitCommand(command);
    }

    private SimpleAggregateId id = new SimpleAggregateId("id");

    private String data = "data";

    private void whenUpdating() {
        TestDomainEvent5 event = runtime().newDomainEvent(TestDomainEvent5.class);
        event.identifier().value(data);
        runtime().issue(event);
        waitUntilEndOfMessageProcessing();
    }

    private void thenAggregateTouched() {
        var aggregate = repository.get(id);
        assertThat(aggregate.attributes().data().value(), equalTo("touched"));
    }

    private SimpleAggregateRepository repository;

    @Test
    public void createWithIdentifierHandler() {
        whenUpdating();
        thenAggregateCreated();
    }

    private void thenAggregateCreated() {
        assertFalse(repository.findByData(data).isEmpty());
    }
}
