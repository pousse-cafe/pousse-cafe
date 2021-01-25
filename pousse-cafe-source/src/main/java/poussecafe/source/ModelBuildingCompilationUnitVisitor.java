package poussecafe.source;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.source.analysis.AggregateContainerClass;
import poussecafe.source.analysis.AggregateRootClass;
import poussecafe.source.analysis.AnnotatedElement;
import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.analysis.CompilationUnitResolver;
import poussecafe.source.analysis.FactoryClass;
import poussecafe.source.analysis.MessageListenerMethod;
import poussecafe.source.analysis.ProcessDefinitionType;
import poussecafe.source.analysis.ProducedEventAnnotation;
import poussecafe.source.analysis.RepositoryClass;
import poussecafe.source.analysis.ResolvedTypeDeclaration;
import poussecafe.source.analysis.ResolvedTypeName;
import poussecafe.source.analysis.RunnerClass;
import poussecafe.source.analysis.TypeResolvingCompilationUnitVisitor;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.Command;
import poussecafe.source.model.DomainEvent;
import poussecafe.source.model.Message;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageListenerContainer;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.model.MessageType;
import poussecafe.source.model.ModelBuilder;
import poussecafe.source.model.ProcessModel;
import poussecafe.source.model.ProducedEvent;
import poussecafe.source.model.Runner;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

class ModelBuildingCompilationUnitVisitor extends TypeResolvingCompilationUnitVisitor {

    @Override
    protected boolean visitTypeDeclarationOrSkip(TypeDeclaration node) {
        var resolvedTypeDeclaration = resolve(node);
        if(AggregateRootClass.isAggregateRoot(resolvedTypeDeclaration)) {
            visitAggregateRoot(resolvedTypeDeclaration);
            return true;
        } else if(FactoryClass.isFactory(resolvedTypeDeclaration)) {
            visitFactory(resolvedTypeDeclaration);
            return true;
        } else if(RepositoryClass.isRepository(resolvedTypeDeclaration)) {
            visitRepository(resolvedTypeDeclaration);
            return true;
        } else if(ProcessDefinitionType.isProcessDefinition(resolvedTypeDeclaration)) {
            visitProcessDefinition(resolvedTypeDeclaration);
            return false;
        } else if(AggregateContainerClass.isAggregateContainerClass(resolvedTypeDeclaration)) {
            visitAggregateContainer(resolvedTypeDeclaration);
            return true;
        } else if(RunnerClass.isRunner(resolvedTypeDeclaration)) {
            visitRunner(resolvedTypeDeclaration);
            return false;
        } else {
            return false;
        }
    }

    private void visitAggregateRoot(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        AggregateRootClass aggregateRootClass = new AggregateRootClass(resolvedTypeDeclaration);
        containerLevel = typeLevel();
        String identifier;
        String aggregateName;
        if(typeLevel() == 0) {
            aggregateName = aggregateRootClass.aggregateName();
            identifier = resolvedTypeDeclaration.name().simpleName();
        } else {
            aggregateName = aggregateNameForInnerClass(resolvedTypeDeclaration);
            identifier = innerClassQualifiedName(resolvedTypeDeclaration);
        }
        createOrUpdateAggregate(aggregateName);
        if(typeLevel() == 0) {
            aggregateBuilder.standaloneRootSource(Optional.of(sourceFile.source()));
        }
        container = new MessageListenerContainer.Builder()
                .type(MessageListenerContainerType.ROOT)
                .aggregateName(aggregateName)
                .containerIdentifier(identifier)
                .build();
    }

    private void visitProcessDefinition(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var processDefinition = new ProcessDefinitionType(resolvedTypeDeclaration);
        modelBuilder.addProcess(new ProcessModel.Builder()
                .name(processDefinition.processName())
                .packageName(compilationUnit().getPackage().getName().getFullyQualifiedName())
                .source(sourceFile.source())
                .build());
    }

    private SourceFile sourceFile;

    private void visitFactory(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        FactoryClass factoryClass = new FactoryClass(resolvedTypeDeclaration);
        containerLevel = typeLevel();
        String identifier;
        String aggregateName;
        if(typeLevel() == 0) {
            aggregateName = factoryClass.aggregateName();
            identifier = factoryClass.simpleName();
        } else {
            aggregateName = aggregateNameForInnerClass(resolvedTypeDeclaration);
            identifier = innerClassQualifiedName(resolvedTypeDeclaration);
        }
        createOrUpdateAggregate(aggregateName);
        if(typeLevel() == 0) {
            aggregateBuilder.standaloneFactorySource(Optional.of(sourceFile.source()));
        }
        container = new MessageListenerContainer.Builder()
                .type(MessageListenerContainerType.FACTORY)
                .aggregateName(aggregateName)
                .containerIdentifier(identifier)
                .build();
    }

    private void visitRepository(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        RepositoryClass repositoryClass = new RepositoryClass(resolvedTypeDeclaration);
        containerLevel = typeLevel();
        String identifier;
        String aggregateName;
        if(typeLevel() == 0) {
            aggregateName = repositoryClass.aggregateName();
            identifier = repositoryClass.simpleName();
        } else {
            aggregateName = aggregateNameForInnerClass(resolvedTypeDeclaration);
            identifier = innerClassQualifiedName(resolvedTypeDeclaration);
        }
        createOrUpdateAggregate(aggregateName);
        if(typeLevel() == 0) {
            aggregateBuilder.standaloneRepositorySource(Optional.of(sourceFile.source()));
        }
        container = new MessageListenerContainer.Builder()
                .type(MessageListenerContainerType.REPOSITORY)
                .aggregateName(aggregateName)
                .containerIdentifier(identifier)
                .build();
    }

    private void createOrUpdateAggregate(String name) {
        aggregateBuilder = modelBuilder.getAndCreateIfAbsent(name,
                compilationUnit().getPackage().getName().getFullyQualifiedName());
    }

    private Aggregate.Builder aggregateBuilder;

    private int containerLevel;

    private MessageListenerContainer container;

    private String aggregateNameForInnerClass(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var typeDeclaration = (TypeDeclaration) resolvedTypeDeclaration.typeDeclaration().getParent();
        return typeDeclaration.getName().getIdentifier();
    }

    private String innerClassQualifiedName(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        return innerClassQualifiedName(resolvedTypeDeclaration.typeDeclaration());
    }

    private String innerClassQualifiedName(TypeDeclaration typeDeclaration) {
        var parent = typeDeclaration.getParent();
        if(parent instanceof CompilationUnit) {
            return typeDeclaration.getName().getIdentifier();
        } else {
            return innerClassQualifiedName((TypeDeclaration) parent) + "." + typeDeclaration.getName().getIdentifier();
        }
    }

    private void visitAggregateContainer(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        AggregateContainerClass containerClass = new AggregateContainerClass(resolvedTypeDeclaration);
        createOrUpdateAggregate(containerClass.aggregateName());
        aggregateBuilder.containerSource(Optional.of(sourceFile.source()));
    }

    @Override
    protected void endTypeDeclarationVisit(TypeDeclaration node) {
        if(typeLevel() == containerLevel
                && aggregateBuilder != null) {
            aggregateBuilder = null;
            container = null;
        }
    }

    private ModelBuilder modelBuilder;

    private void visitRunner(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var runnerClass = new RunnerClass(resolvedTypeDeclaration);
        modelBuilder.addRunner(new Runner.Builder()
                .withRunnerSource(Optional.of(sourceFile.source()))
                .withClassName(runnerClass.className())
                .build());
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        if(aggregateBuilder != null) {
            var method = currentResolver().resolve(node);
            var annotatedMethod = method.asAnnotatedElement();
            if(MessageListenerMethod.isMessageListener(method)) {
                var listenerMethod = new MessageListenerMethod(method);
                var messageListener = new MessageListener.Builder()
                        .withContainer(container)
                        .withMethodDeclaration(listenerMethod)
                        .withRunnerClass(listenerMethod.runner().map(ResolvedTypeName::qualifiedName))
                        .build();
                modelBuilder.addMessageListener(messageListener);

                registerMessage(messageListener.consumedMessage(),
                        method.parameterTypeName(0).orElseThrow());
                registerMessages(messageListener.producedEvents(), annotatedMethod);
            } else if(container.type() == MessageListenerContainerType.ROOT) {
                if(method.name().equals(Aggregate.ON_ADD_METHOD_NAME)) {
                    var producedEvents = producesEvents(annotatedMethod);
                    aggregateBuilder.onAddProducedEvents(producedEvents);
                    registerMessages(producedEvents, annotatedMethod);
                } else if(method.name().equals(Aggregate.ON_DELETE_METHOD_NAME)) {
                    var producedEvents = producesEvents(annotatedMethod);
                    aggregateBuilder.onDeleteProducedEvents(producedEvents);
                    registerMessages(producedEvents, annotatedMethod);
                }
            }
        }
        return false;
    }

    private void registerMessage(Message message, ResolvedTypeName messageTypeName) {
        if(message.type() == MessageType.COMMAND) {
            modelBuilder.addCommand(new Command.Builder()
                    .name(message.name())
                    .packageName(messageTypeName.packageName())
                    .source(messageTypeName.resolvedClass().source())
                    .build());
        } else if(message.type() == MessageType.DOMAIN_EVENT) {
            modelBuilder.addEvent(new DomainEvent.Builder()
                    .name(message.name())
                    .packageName(messageTypeName.packageName())
                    .source(messageTypeName.resolvedClass().source())
                    .build());
        } else {
            throw new UnsupportedOperationException("Unsupported message type " + message.type());
        }
    }

    private void registerMessages(List<ProducedEvent> producedEvents,
            AnnotatedElement<MethodDeclaration> method) {
        var producedEventAnnotations = method.findAnnotations(
                CompilationUnitResolver.PRODUCES_EVENT_ANNOTATION_CLASS).stream()
                .map(ProducedEventAnnotation::new)
                .map(ProducedEventAnnotation::event)
                .collect(toList());
        for(int i = 0; i < producedEvents.size(); ++i) {
            var producedEvent = producedEvents.get(i);
            var message = producedEvent.message();
            var eventType = producedEventAnnotations.get(i);
            registerMessage(message, eventType);
        }
    }

    private List<ProducedEvent> producesEvents(AnnotatedElement<MethodDeclaration> method) {
        return method.findAnnotations(CompilationUnitResolver.PRODUCES_EVENT_ANNOTATION_CLASS)
                .stream()
                .map(ProducedEventAnnotation::new)
                .map(annotation -> new ProducedEvent.Builder().withAnnotation(annotation).build())
                .collect(toList());
    }

    public static class Builder {

        public ModelBuildingCompilationUnitVisitor build() {
            requireNonNull(sourceFile);

            var compilationUnit = sourceFile.tree();
            var compilationUnitResolver = new CompilationUnitResolver.Builder()
                    .compilationUnit(compilationUnit)
                    .classResolver(classResolver)
                    .build();
            var visitor = new ModelBuildingCompilationUnitVisitor(compilationUnitResolver);
            visitor.sourceFile = sourceFile;
            visitor.modelBuilder = modelBuilder;
            return visitor;
        }

        public Builder sourceFile(SourceFile sourceFile) {
            this.sourceFile = sourceFile;
            return this;
        }

        private SourceFile sourceFile;

        public Builder classResolver(ClassResolver classResolver) {
            this.classResolver = classResolver;
            return this;
        }

        private ClassResolver classResolver;

        public Builder modelBuilder(ModelBuilder modelBuilder) {
            this.modelBuilder = modelBuilder;
            return this;
        }

        private ModelBuilder modelBuilder;
    }

    private ModelBuildingCompilationUnitVisitor(CompilationUnitResolver compilationUnit) {
        super(compilationUnit);
    }
}
