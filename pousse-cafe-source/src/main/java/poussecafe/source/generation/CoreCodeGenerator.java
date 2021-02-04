package poussecafe.source.generation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.preferences.IScopeContext;
import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.analysis.Name;
import poussecafe.source.analysis.SourceModelBuilder;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.DefaultInsertionMode;
import poussecafe.source.generation.tools.InsertionMode;
import poussecafe.source.generation.tools.TypeDeclarationEditor;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.Command;
import poussecafe.source.model.DomainEvent;
import poussecafe.source.model.MessageListener;
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
        if(aggregate.requiresContainer()) {
            addAggregateContainer(aggregate);
        }
        addAggregateId(aggregate);
        addAggregateRoot(aggregate);
        addAggregateDataAccess(aggregate);
        addAggregateFactory(aggregate);
        addAggregateRepository(aggregate);
        addAttributesDefaultImplementation(aggregate);
    }

    private void addAggregateContainer(Aggregate aggregate) {
        var typeName = NamingConventions.aggregateContainerTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateRootEditor = new AggregateContainerEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateRootEditor.edit();
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
        var compilationUnitEditor = containerOrStandaloneCompilationUnitEditor(aggregate,
                aggregate.innerRoot(),
                NamingConventions.aggregateRootTypeName(aggregate));

        var pivots = new ArrayList<PivotType>();
        pivots.add(new PivotType(NamingConventions.innerFactoryClassName(), InsertionMode.AFTER));
        pivots.add(new PivotType(NamingConventions.innerRepositoryClassName(), InsertionMode.BEFORE));

        var typeEditor = containerOrStanaloneTypeDeclarationEditor(compilationUnitEditor,
                aggregate.innerRoot(),
                NamingConventions.innerRootClassName(),
                pivots);

        var aggregateRootEditor = new AggregateRootEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .typeEditor(typeEditor)
                .build();
        aggregateRootEditor.edit();
    }

    private CompilationUnitEditor containerOrStandaloneCompilationUnitEditor(Aggregate aggregate,
            boolean innerType,
            Name standaloneTypeName) {
        if(innerType) {
            var typeName = NamingConventions.aggregateContainerTypeName(aggregate);
            return compilationUnitEditor(typeName);
        } else {
            return compilationUnitEditor(standaloneTypeName);
        }
    }

    private TypeDeclarationEditor containerOrStanaloneTypeDeclarationEditor(
            CompilationUnitEditor compilationUnitEditor,
            boolean innerType,
            String innertTypeName,
            List<PivotType> pivots) {
        if(innerType) {
            var containerClass = compilationUnitEditor.typeDeclaration();
            return insertNewInnerType(containerClass,
                    innertTypeName,
                    pivots);
        } else {
            return compilationUnitEditor.typeDeclaration();
        }
    }

    private TypeDeclarationEditor insertNewInnerType(
            TypeDeclarationEditor containerClass,
            String typeName,
            List<PivotType> pivots) {
        var existingRoot = containerClass.findTypeDeclarationByName(typeName);
        if(existingRoot.isPresent()) {
            return containerClass.editExistingType(existingRoot.get());
        } else {
            for(PivotType pivot : pivots) {
                var pivotType = containerClass.findTypeDeclarationByName(pivot.name);
                if(pivotType.isPresent()) {
                    if(pivot.insertionMode == InsertionMode.AFTER) {
                        return containerClass.newTypeDeclarationAfter(typeName, pivotType.get());
                    } else if(pivot.insertionMode == InsertionMode.BEFORE) {
                        return containerClass.newTypeDeclarationBefore(typeName, pivotType.get());
                    } else {
                        throw new UnsupportedOperationException();
                    }
                }
            }
            return containerClass.declaredType(typeName, DefaultInsertionMode.FIRST);
        }
    }

    private class PivotType {

        String name;

        InsertionMode insertionMode;

        PivotType(String name, InsertionMode insertionMode) {
            this.name = name;
            this.insertionMode = insertionMode;
        }
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
        var compilationUnitEditor = containerOrStandaloneCompilationUnitEditor(aggregate,
                aggregate.innerFactory(),
                NamingConventions.aggregateFactoryTypeName(aggregate));

        var pivots = new ArrayList<PivotType>();
        pivots.add(new PivotType(NamingConventions.innerRootClassName(), InsertionMode.BEFORE));
        pivots.add(new PivotType(NamingConventions.innerRepositoryClassName(), InsertionMode.BEFORE));

        var typeEditor = containerOrStanaloneTypeDeclarationEditor(compilationUnitEditor,
                aggregate.innerFactory(),
                NamingConventions.innerFactoryClassName(),
                pivots);

        var aggregateFactoryEditor = new AggregateFactoryEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .typeEditor(typeEditor)
                .build();
        aggregateFactoryEditor.edit();
    }

    private void addAggregateRepository(Aggregate aggregate) {
        var compilationUnitEditor = containerOrStandaloneCompilationUnitEditor(aggregate,
                aggregate.innerRepository(),
                NamingConventions.aggregateRepositoryTypeName(aggregate));

        var pivots = new ArrayList<PivotType>();
        pivots.add(new PivotType(NamingConventions.innerRootClassName(), InsertionMode.AFTER));
        pivots.add(new PivotType(NamingConventions.innerFactoryClassName(), InsertionMode.AFTER));

        var typeEditor = containerOrStanaloneTypeDeclarationEditor(compilationUnitEditor,
                aggregate.innerRepository(),
                NamingConventions.innerRepositoryClassName(),
                pivots);

        var aggregateRepositoryEditor = new AggregateRepositoryEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .typeEditor(typeEditor)
                .build();
        aggregateRepositoryEditor.edit();
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
            var compilationUnitEditor = containerOrStandaloneCompilationUnitEditor(aggregate,
                    aggregate.innerRoot(),
                    NamingConventions.aggregateRootTypeName(aggregate));
            var typeEditor = stanaloneOrInnerTypeDeclarationEditor(compilationUnitEditor,
                    aggregate.innerRoot(),
                    NamingConventions.innerRootClassName());
            var aggregateRootEditor = new AggregateRootHooksEditor.Builder()
                    .compilationUnitEditor(compilationUnitEditor)
                    .aggregate(aggregate)
                    .model(model)
                    .typeEditor(typeEditor)
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
            if(containerType.isRoot()) {
                generateAggregateRootListeners(model, listener, aggregate);
                generateRunner(model, listener, aggregate);
            } else if(containerType.isFactory()) {
                generateFactoryListeners(model, listener, aggregate);
            } else if(containerType.isRepository()) {
                generateRepositoryListeners(model, listener, aggregate);
            } else {
                throw new UnsupportedOperationException("Unsupported container type " + containerType);
            }
        }
    }

    private void generateAggregateRootListeners(Model model, MessageListener listener, Aggregate aggregate) {
        var compilationUnitEditor = containerOrStandaloneCompilationUnitEditor(aggregate,
                aggregate.innerRoot(),
                NamingConventions.aggregateRootTypeName(aggregate));
        var typeEditor = stanaloneOrInnerTypeDeclarationEditor(compilationUnitEditor,
                aggregate.innerRoot(),
                NamingConventions.innerRootClassName());
        var editor = new AggregateRootMessageListenerEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .model(model)
                .messageListener(listener)
                .typeEditor(typeEditor)
                .build();
        editor.edit();
    }

    private TypeDeclarationEditor stanaloneOrInnerTypeDeclarationEditor(
            CompilationUnitEditor compilationUnitEditor,
            boolean innerType,
            String innertTypeName) {
        if(innerType) {
            var containerClass = compilationUnitEditor.typeDeclaration();
            return innerType(containerClass,
                    innertTypeName);
        } else {
            return compilationUnitEditor.typeDeclaration();
        }
    }

    private TypeDeclarationEditor innerType(TypeDeclarationEditor containerClass, String innertTypeName) {
        var existingRoot = containerClass.findTypeDeclarationByName(innertTypeName);
        return containerClass.editExistingType(existingRoot.orElseThrow());
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
        var compilationUnitEditor = containerOrStandaloneCompilationUnitEditor(aggregate,
                aggregate.innerFactory(),
                NamingConventions.aggregateFactoryTypeName(aggregate));
        var typeEditor = stanaloneOrInnerTypeDeclarationEditor(compilationUnitEditor,
                aggregate.innerFactory(),
                NamingConventions.innerFactoryClassName());
        var editor = new AggregateFactoryMessageListenerEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .model(model)
                .messageListener(listener)
                .aggregate(aggregate)
                .typeEditor(typeEditor)
                .build();
        editor.edit();
    }

    private void generateRepositoryListeners(Model model, MessageListener listener, Aggregate aggregate) {
        var compilationUnitEditor = containerOrStandaloneCompilationUnitEditor(aggregate,
                aggregate.innerRepository(),
                NamingConventions.aggregateRepositoryTypeName(aggregate));
        var typeEditor = stanaloneOrInnerTypeDeclarationEditor(compilationUnitEditor,
                aggregate.innerRepository(),
                NamingConventions.innerRepositoryClassName());
        var editor = new AggregateRepositoryMessageListenerEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .model(model)
                .messageListener(listener)
                .typeEditor(typeEditor)
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
                    var modelBuilder = new SourceModelBuilder(classResolver);
                    modelBuilder.includeTree(generator.sourceDirectory);
                    generator.currentModel = modelBuilder.build();
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

        public Builder preferencesContext(IScopeContext context) {
            generator.loadPreferencesFromContext(context);
            return this;
        }

        public Builder classResolver(ClassResolver classResolver) {
            this.classResolver = classResolver;
            return this;
        }

        private ClassResolver classResolver;
    }

    private CoreCodeGenerator() {

    }
}
