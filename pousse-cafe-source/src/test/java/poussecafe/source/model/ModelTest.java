package poussecafe.source.model;

import org.junit.Test;
import poussecafe.source.PathSource;
import poussecafe.source.analysis.Name;
import poussecafe.source.analysis.SafeClassName;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class ModelTest {

    @Test
    public void fixPackageNames() {
        givenCurrentModel();
        givenNewModelWithOtherPackageNames();
        whenFixingModel();
        thenFixedModelHasCurrentModelPackageNames();
        thenFixedModelContainsOnlyComponentsFromNewModel();
    }

    private void givenCurrentModel() {
        setModel(currentModel, CURRENT_PACKAGE);

        currentModel.addCommand(new Command.Builder()
                .name("Command1")
                .packageName(CURRENT_PACKAGE)
                .build());

        currentModel.addEvent(new DomainEvent.Builder()
                .name("Event1")
                .packageName(CURRENT_PACKAGE)
                .build());
    }

    private static final String CURRENT_PACKAGE = "current.package";

    private void setModel(Model currentModel, String packageName) {
        currentModel.addCommand(new Command.Builder()
                .name("Command1")
                .packageName(packageName)
                .build());
        currentModel.addEvent(new DomainEvent.Builder()
                .name("Event1")
                .packageName(packageName)
                .build());
        currentModel.addAggregate(new Aggregate.Builder()
                .name("Aggregate1")
                .packageName(packageName)
                .ensureDefaultLocations()
                .build());
        var containerTypeName = SafeClassName.ofRootClass(new Name(CURRENT_PACKAGE, "Aggregate1Factory"));
        currentModel.addMessageListener(new MessageListener.Builder()
                .withContainer(new MessageListenerContainer.Builder()
                        .type(MessageListenerContainerType.STANDALONE_ROOT)
                        .aggregateName("Aggregate1")
                        .containerIdentifier("Aggregate1Factory")
                        .build())
                .withMethodName("method")
                .withConsumedMessage(new Message.Builder()
                        .type(MessageType.COMMAND)
                        .name("Command1")
                        .build())
                .withProcessName("Process1")
                .withProducedEvent(new ProducedEvent.Builder()
                        .message(new Message.Builder()
                            .type(MessageType.DOMAIN_EVENT)
                            .name("Event1")
                            .build())
                        .build())
                .withSource(new PathSource(containerTypeName.toRelativePath()))
                .build());
    }

    private Model currentModel = new Model();

    private void givenNewModelWithOtherPackageNames() {
        setModel(newModel, "other.package");
    }

    private Model newModel = new Model();

    private void whenFixingModel() {
        fixedModel = currentModel.fixPackageNames(newModel);
    }

    private Model fixedModel;

    private void thenFixedModelHasCurrentModelPackageNames() {
        for(Command command : fixedModel.commands()) {
            assertThat(command.packageName(), equalTo(CURRENT_PACKAGE));
        }
        for(DomainEvent event : fixedModel.events()) {
            assertThat(event.packageName(), equalTo(CURRENT_PACKAGE));
        }
        for(Aggregate aggregate : fixedModel.aggregates()) {
            assertThat(aggregate.packageName(), equalTo(CURRENT_PACKAGE));
        }
    }

    private void thenFixedModelContainsOnlyComponentsFromNewModel() {
        for(Command command : fixedModel.commands()) {
            assertTrue(newModel.command(command.simpleName()).isPresent());
        }
        for(DomainEvent event : fixedModel.events()) {
            assertTrue(newModel.event(event.simpleName()).isPresent());
        }
        for(Aggregate aggregate : fixedModel.aggregates()) {
            assertTrue(newModel.aggregate(aggregate.simpleName()).isPresent());
        }
        for(MessageListener listener : fixedModel.messageListeners()) {
            assertTrue(newModel.messageListeners().contains(listener));
        }
    }
}
