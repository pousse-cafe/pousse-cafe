package poussecafe.source.generation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import poussecafe.source.Scanner;
import poussecafe.source.analysis.Name;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.Command;
import poussecafe.source.model.DomainEvent;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.model.Model;
import poussecafe.source.model.ProcessModel;

import static java.util.Objects.requireNonNull;

public class CoreCodeGenerator extends AbstractCodeGenerator {

    public void generate(Model newModel) {
        var fixedModel = currentModel.fixPackageNames(newModel);
        generateWithFixedModel(fixedModel);
    }

    private void generateWithFixedModel(Model fixedModel) {
        for(ProcessModel process : fixedModel.processes()) {
            if(currentModel.process(process.simpleName()).isEmpty()) {
                generateProcess(process);
            }
        }

        for(Aggregate aggregate : fixedModel.aggregates()) {
            if(currentModel.aggregate(aggregate.simpleName()).isEmpty()) {
                generate(aggregate);
            }
        }

        for(Command command : fixedModel.commands()) {
            if(currentModel.command(command.simpleName()).isEmpty()) {
                generateCommand(command);
            }
        }

        for(DomainEvent event : fixedModel.events()) {
            if(currentModel.event(event.simpleName()).isEmpty()) {
                generateEvent(event);
            }
        }

        generateHooks(fixedModel);
        generateMessageListeners(fixedModel);
    }

    private Model currentModel;

    private void generateProcess(ProcessModel process) {
        var typeName = process.name();
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateRootEditor = new ProcessEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .process(process)
                .build();
        aggregateRootEditor.edit();
    }

    public void generate(Aggregate aggregate) {
        addAggregateId(aggregate);
        addAggregateRoot(aggregate);
        addAggregateDataAccess(aggregate);
        addAggregateFactory(aggregate);
        addAggregateRepository(aggregate);
        addAttributesDefaultImplementation(aggregate);
    }

    private void addAggregateId(Aggregate aggregate) {
        var typeName = NamingConventions.aggregateIdentifierTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateRootEditor = new AggregateIdEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateRootEditor.edit();
    }

    private void addAggregateRoot(Aggregate aggregate) {
        var typeName = NamingConventions.aggregateRootTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateRootEditor = new AggregateRootEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateRootEditor.edit();
    }

    private void addAggregateDataAccess(Aggregate aggregate) {
        var typeName = NamingConventions.aggregateDataAccessTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateRootEditor = new AggregateDataAccessEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateRootEditor.edit();
    }

    private void addAggregateFactory(Aggregate aggregate) {
        var typeName = NamingConventions.aggregateFactoryTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateFactoryEditor = new AggregateFactoryEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateFactoryEditor.edit();
    }

    private void addAggregateRepository(Aggregate aggregate) {
        var typeName = NamingConventions.aggregateRepositoryTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateFactoryEditor = new AggregateRepositoryEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateFactoryEditor.edit();
    }

    private void addAttributesDefaultImplementation(Aggregate aggregate) {
        var typeName = NamingConventions.aggregateAttributesImplementationTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateFactoryEditor = new AggregateAttributesImplementationEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateFactoryEditor.edit();
    }

    private void generateCommand(Command command) {
        generateCommandDefinition(command);
        generateCommandImplementation(command);
    }

    private void generateCommandDefinition(Command command) {
        var typeName = command.name();
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var commandEditor = new CommandEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .command(command)
                .build();
        commandEditor.edit();
    }

    private void generateCommandImplementation(Command command) {
        var typeName = NamingConventions.commandImplementationTypeName(command);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var commandEditor = new CommandImplementationEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .command(command)
                .build();
        commandEditor.edit();
    }

    private void generateEvent(DomainEvent event) {
        generateEventDefinition(event);
        generateEventImplementation(event);
    }

    private void generateEventDefinition(DomainEvent event) {
        var typeName = event.name();
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var commandEditor = new EventEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .event(event)
                .build();
        commandEditor.edit();
    }

    private void generateEventImplementation(DomainEvent event) {
        var typeName = NamingConventions.eventImplementationTypeName(event);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var commandEditor = new EventImplementationEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .event(event)
                .build();
        commandEditor.edit();
    }

    private void generateHooks(Model model) {
        for(Aggregate aggregate : model.aggregates()) {
            var typeName = NamingConventions.aggregateRootTypeName(aggregate);
            var compilationUnitEditor = compilationUnitEditor(typeName);
            var aggregateRootEditor = new AggregateRootHooksEditor.Builder()
                    .compilationUnitEditor(compilationUnitEditor)
                    .aggregate(aggregate)
                    .model(model)
                    .build();
            aggregateRootEditor.edit();
        }
    }

    private void generateMessageListeners(Model model) {
        for(MessageListener listener : model.messageListeners()) {
            var containerType = listener.container().type();
            var aggregateName = listener.container().aggregateName().orElseThrow();
            var aggregate = model.aggregate(aggregateName)
                    .orElseThrow(() -> new IllegalStateException("No aggregate with name " + aggregateName));
            if(containerType == MessageListenerContainerType.ROOT) {
                generateAggregateRootListeners(model, listener, aggregate);
                generateRunner(model, listener, aggregate);
            } else if(containerType == MessageListenerContainerType.FACTORY) {
                generateFactoryListeners(model, listener, aggregate);
            } else if(containerType == MessageListenerContainerType.REPOSITORY) {
                generateRepositoryListeners(model, listener, aggregate);
            } else {
                throw new UnsupportedOperationException("Unsupported container type " + containerType);
            }
        }
    }

    private void generateAggregateRootListeners(Model model, MessageListener listener, Aggregate aggregate) {
        var typeName = NamingConventions.aggregateRootTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var editor = new AggregateRootMessageListenerEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .model(model)
                .messageListener(listener)
                .build();
        editor.edit();
    }

    private void generateRunner(Model model, MessageListener listener, Aggregate aggregate) {
        var runnerTypeName = new Name(NamingConventions.runnerPackage(aggregate),
                listener.runnerName().orElseThrow());
        var runnerCompilationUnitEditor = compilationUnitEditor(runnerTypeName);
        var runnerEditor = new RunnerEditor.Builder()
                .compilationUnitEditor(runnerCompilationUnitEditor)
                .model(model)
                .aggregate(aggregate)
                .messageListener(listener)
                .build();
        runnerEditor.edit();
    }

    private void generateFactoryListeners(Model model, MessageListener listener, Aggregate aggregate) {
        var typeName = NamingConventions.aggregateFactoryTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var editor = new AggregateFactoryMessageListenerEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .model(model)
                .messageListener(listener)
                .aggregate(aggregate)
                .build();
        editor.edit();
    }

    private void generateRepositoryListeners(Model model, MessageListener listener, Aggregate aggregate) {
        var typeName = NamingConventions.aggregateRepositoryTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var editor = new AggregateRepositoryMessageListenerEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .model(model)
                .messageListener(listener)
                .build();
        editor.edit();
    }

    public static class Builder {

        private CoreCodeGenerator generator = new CoreCodeGenerator();

        public CoreCodeGenerator build() {
            requireNonNull(generator.sourceDirectory);
            requireNonNull(generator.formatterOptions);

            if(generator.currentModel == null) {
                try {
                    var scanner = new Scanner();
                    scanner.includeTree(generator.sourceDirectory);
                    generator.currentModel = scanner.model();
                } catch (NoSuchFileException e) {
                    generator.currentModel = new Model(); // empty model
                } catch (IOException e) {
                    throw new IllegalArgumentException("Unable to build model from source directory " +
                            generator.sourceDirectory);
                }
            }

            return generator;
        }

        public Builder sourceDirectory(Path sourceDirectory) {
            generator.sourceDirectory = sourceDirectory;
            return this;
        }

        public Builder currentModel(Model currentModel) {
            generator.currentModel = currentModel;
            return this;
        }

        public Builder codeFormatterProfile(Path profile) {
            generator.loadProfileFromFile(profile);
            return this;
        }

        public Builder codeFormatterProfile(InputStream inputStream) {
            generator.loadProfileFromFile(inputStream);
            return this;
        }
    }

    private CoreCodeGenerator() {

    }
}
