package poussecafe.processing;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import poussecafe.messaging.sync.SynchronousMessaging;
import poussecafe.runtime.ProcessingMode;
import poussecafe.runtime.Runtime;
import poussecafe.storage.internal.InternalStorage;
import poussecafe.testmodule.CreateSimpleAggregate;
import poussecafe.testmodule.SimpleAggregateId;
import poussecafe.testmodule.SimpleAggregateRepository;
import poussecafe.testmodule.TestModuleBundle;
import poussecafe.testmodule.UpdateSimpleAggregate;

public class SynchronousProcessingTest {

    @Before
    public void setup() {
        runtime = new Runtime.Builder()
                .processingMode(ProcessingMode.synchronous())
                .withBundle(TestModuleBundle.configurer()
                        .defineThenImplement()
                        .messaging(SynchronousMessaging.instance())
                        .storage(InternalStorage.instance())
                        .build())
                .build();
        runtime.injector().injectDependenciesInto(this);
        runtime.registerListenersOf(this);
        runtime.start();
    }

    private Runtime runtime;

    @Test
    public void singleCommandIsProcessed() {
        var command = runtime.newCommand(CreateSimpleAggregate.class);
        var id = new SimpleAggregateId("id");
        command.identifier().value(id);
        command.data().value("data");

        runtime.submitCommand(command);

        assertTrue(repository.existsById(id));
    }

    private SimpleAggregateRepository repository;

    @Test
    public void commandSequenceIsProcessed() {
        var command1 = runtime.newCommand(CreateSimpleAggregate.class);
        var id = new SimpleAggregateId("id");
        command1.identifier().value(id);
        command1.data().value("data");
        runtime.submitCommand(command1);

        var command2 = runtime.newCommand(UpdateSimpleAggregate.class);
        command2.identifier().value(id);
        runtime.submitCommand(command2);

        var aggregate = repository.get(id);
        assertThat(aggregate.attributes().data().value(), equalTo("touched"));
    }
}
