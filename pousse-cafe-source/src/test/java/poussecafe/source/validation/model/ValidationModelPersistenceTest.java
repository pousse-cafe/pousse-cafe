package poussecafe.source.validation.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.Test;
import poussecafe.messaging.internal.InternalMessaging;
import poussecafe.source.PathSource;
import poussecafe.source.analysis.Name;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.validation.SourceLine;
import poussecafe.storage.internal.InternalStorage;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ValidationModelPersistenceTest {

    @Test
    public void serializable() throws IOException, ClassNotFoundException {
        givenModel();
        whenSerializeDeserialize();
        thenDeserializedModelMatchesExpected();
    }

    private void givenModel() {
        model = new ValidationModel();

        messageDefinition = new MessageDefinition.Builder()
                .domainEvent(true)
                .messageName("Event")
                .qualifiedClassName("package.Event")
                .sourceLine(new SourceLine.Builder()
                        .source(new PathSource(Path.of("package/Event.java")))
                        .line(42)
                        .build())
                .build();
        model.addMessageDefinition(messageDefinition);

        messageImplementation = new MessageImplementation.Builder()
                .className(new Name("package.EventData"))
                .concrete(true)
                .implementsMessage(true)
                .messageDefinitionClassName(new Name("package.Event"))
                .messagingNames(asList(InternalMessaging.NAME))
                .sourceLine(new SourceLine.Builder()
                        .source(new PathSource(Path.of("package/EventData.java")))
                        .line(42)
                        .build())
                .build();
        model.addMessageImplementation(messageImplementation);

        entityDefinition = new EntityDefinition.Builder()
                .className(new Name("package.Entity"))
                .entityName("Entity")
                .sourceLine(new SourceLine.Builder()
                        .source(new PathSource(Path.of("package/Entity.java")))
                        .line(42)
                        .build())
                .build();
        model.addEntityDefinition(entityDefinition);

        entityImplementation = new EntityImplementation.Builder()
                .entityDefinitionQualifiedClassName(Optional.of("package.Entity"))
                .entityImplementationQualifiedClassName(Optional.of("package.EntityAttributes"))
                .sourceFileLine(new SourceLine.Builder()
                        .source(new PathSource(Path.of("package/EntityAttributes.java")))
                        .line(42)
                        .build())
                .storageNames(asList(InternalStorage.NAME))
                .build();
        model.addEntityImplementation(entityImplementation);

        messageListener = new MessageListener.Builder()
                .consumedMessageClass(Optional.of(new Name("package.Event")))
                .containerType(MessageListenerContainerType.INNER_ROOT)
                .isPublic(true)
                .parametersCount(1)
                .returnsValue(false)
                .runnerQualifiedClassName(Optional.of("package.Runner"))
                .sourceLine(new SourceLine.Builder()
                        .source(new PathSource(Path.of("package/Aggregate.java")))
                        .line(42)
                        .build())
                .build();
        model.addMessageListener(messageListener);

        runner = new Runner.Builder()
                .classQualifiedName("package.Runner")
                .sourceFileLine(new SourceLine.Builder()
                        .source(new PathSource(Path.of("package/Aggregate.java")))
                        .line(42)
                        .build())
                .typeParametersQualifiedNames(asList("package.Event"))
                .build();
        model.addRunner(runner);

        module = new Module.Builder()
                .className(new Name("package.Module"))
                .sourceLine(new SourceLine.Builder()
                        .source(new PathSource(Path.of("package/Aggregate.java")))
                        .line(42)
                        .build())
                .build();
        model.addModule(module);

        processDefinition = new ProcessDefinition.Builder()
                .className(new Name("package.Process"))
                .name("Process")
                .sourceLine(new SourceLine.Builder()
                        .source(new PathSource(Path.of("package/Process.java")))
                        .line(42)
                        .build())
                .build();
        model.addProcessDefinition(processDefinition);

        aggregateFactory = new AggregateComponentDefinition.Builder()
                .className(new Name("package.Aggregate.Factory"))
                .innerClass(true)
                .sourceLine(new SourceLine.Builder()
                        .source(new PathSource(Path.of("package/Aggregate.java")))
                        .line(41)
                        .build())
                .kind(AggregateComponentKind.FACTORY)
                .build();
        model.addAggregateFactory(aggregateFactory);

        aggregateRoot = new AggregateComponentDefinition.Builder()
                .className(new Name("package.Aggregate.Root"))
                .innerClass(true)
                .sourceLine(new SourceLine.Builder()
                        .source(new PathSource(Path.of("package/Aggregate.java")))
                        .line(42)
                        .build())
                .kind(AggregateComponentKind.ROOT)
                .build();
        model.addAggregateRoot(aggregateRoot);

        aggregateRepository = new AggregateComponentDefinition.Builder()
                .className(new Name("package.Aggregate.Repository"))
                .innerClass(true)
                .sourceLine(new SourceLine.Builder()
                        .source(new PathSource(Path.of("package/Aggregate.java")))
                        .line(43)
                        .build())
                .kind(AggregateComponentKind.REPOSITORY)
                .build();
        model.addAggregateRepository(aggregateRepository);

        dataAccessDefinition = new DataAccessDefinition.Builder()
                .sourceLine(new SourceLine.Builder()
                        .source(new PathSource(Path.of("package/AggregateDataAccess.java")))
                        .line(43)
                        .build())
                .className(new Name("package.AggregateData"))
                .build();
    }

    private ValidationModel model;

    private MessageDefinition messageDefinition;

    private MessageImplementation messageImplementation;

    private EntityDefinition entityDefinition;

    private EntityImplementation entityImplementation;

    private MessageListener messageListener;

    private Runner runner;

    private Module module;

    private ProcessDefinition processDefinition;

    private AggregateComponentDefinition aggregateRoot;

    private AggregateComponentDefinition aggregateFactory;

    private AggregateComponentDefinition aggregateRepository;

    private DataAccessDefinition dataAccessDefinition;

    private void whenSerializeDeserialize() throws IOException, ClassNotFoundException {
        var bytes = serialize();
        deserialize(bytes);
    }

    private ByteArrayOutputStream serialize() throws IOException {
        var bytes = new ByteArrayOutputStream();
        var oos = new ObjectOutputStream(bytes);
        oos.writeObject(model);
        oos.close();
        return bytes;
    }

    private void deserialize(ByteArrayOutputStream bytes) throws IOException, ClassNotFoundException {
        var ois = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        deserializedModel = (ValidationModel) ois.readObject();
    }

    private ValidationModel deserializedModel;

    private void thenDeserializedModelMatchesExpected() {
        assertEquals(model, deserializedModel);
    }
}
