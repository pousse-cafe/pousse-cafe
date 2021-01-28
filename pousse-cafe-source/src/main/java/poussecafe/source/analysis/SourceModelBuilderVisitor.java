package poussecafe.source.analysis;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.AggregateContainer;
import poussecafe.source.model.Command;
import poussecafe.source.model.DomainEvent;
import poussecafe.source.model.Hooks;
import poussecafe.source.model.Message;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageListenerContainer;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.model.MessageType;
import poussecafe.source.model.Model;
import poussecafe.source.model.ModelBuilder;
import poussecafe.source.model.ProcessModel;
import poussecafe.source.model.ProducedEvent;
import poussecafe.source.model.Runner;
import poussecafe.source.model.StandaloneAggregateFactory;
import poussecafe.source.model.StandaloneAggregateRepository;
import poussecafe.source.model.StandaloneAggregateRoot;
import poussecafe.source.model.TypeComponent;

import static java.util.stream.Collectors.toList;

public class SourceModelBuilderVisitor implements ResolvedCompilationUnitVisitor {

    @Override
    public boolean visit(ResolvedCompilationUnit unit) {
        foundPousseCafeComponent(false);
        compilationUnit = unit;
        return false;
    }

    private int typeLevel = -1;

    private ResolvedCompilationUnit compilationUnit;

    @Override
    public boolean visit(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        ++typeLevel;
        if(AggregateRootClass.isAggregateRoot(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            visitAggregateRoot(resolvedTypeDeclaration);
            return true;
        } else if(FactoryClass.isFactory(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            visitFactory(resolvedTypeDeclaration);
            return true;
        } else if(RepositoryClass.isRepository(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            visitRepository(resolvedTypeDeclaration);
            return true;
        } else if(ProcessDefinitionType.isProcessDefinition(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            visitProcessDefinition(resolvedTypeDeclaration);
            return false;
        } else if(AggregateContainerClass.isAggregateContainerClass(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            visitAggregateContainer(resolvedTypeDeclaration);
            return true;
        } else if(RunnerClass.isRunner(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            visitRunner(resolvedTypeDeclaration);
            return false;
        } else {
            return false;
        }
    }

    private void visitAggregateRoot(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        AggregateRootClass aggregateRootClass = new AggregateRootClass(resolvedTypeDeclaration);
        containerLevel = typeLevel;
        String identifier;
        String aggregateName;
        if(typeLevel == 0) {
            aggregateName = aggregateRootClass.aggregateName();
            identifier = resolvedTypeDeclaration.name().simpleName();
        } else {
            aggregateName = aggregateNameForInnerClass(resolvedTypeDeclaration);
            identifier = innerClassQualifiedName(resolvedTypeDeclaration);
        }
        if(typeLevel == 0) {
            createStandaloneAggregateRoot(resolvedTypeDeclaration);
        }
        container = new MessageListenerContainer.Builder()
                .type(typeLevel == 0 ? MessageListenerContainerType.STANDALONE_ROOT : MessageListenerContainerType.INNER_ROOT)
                .aggregateName(aggregateName)
                .containerIdentifier(identifier)
                .build();
    }

    private void createStandaloneAggregateRoot(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        standaloneAggregateRootBuilder = new StandaloneAggregateRoot.Builder();
        standaloneAggregateRootBuilder.typeComponent(typeComponent(resolvedTypeDeclaration.unresolvedName()));
        hooksBuilder = new Hooks.Builder();
    }

    private TypeComponent typeComponent(SafeClassName typeName) {
        return new TypeComponent.Builder()
                .source(compilationUnit.sourceFile().source())
                .name(typeName)
                .build();
    }

    private StandaloneAggregateRoot.Builder standaloneAggregateRootBuilder;

    private Hooks.Builder hooksBuilder;

    @Override
    public boolean foundContent() {
        return foundPousseCafeComponent;
    }

    private boolean foundPousseCafeComponent;

    protected void foundPousseCafeComponent(boolean value) {
        foundPousseCafeComponent = value;
    }

    private void visitProcessDefinition(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var processDefinition = new ProcessDefinitionType(resolvedTypeDeclaration);
        modelBuilder.addProcess(new ProcessModel.Builder()
                .name(processDefinition.processName())
                .packageName(compilationUnit.packageName())
                .source(compilationUnit.sourceFile().source())
                .build());
    }

    private void visitFactory(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        FactoryClass factoryClass = new FactoryClass(resolvedTypeDeclaration);
        containerLevel = typeLevel;
        String identifier;
        String aggregateName;
        if(typeLevel == 0) {
            aggregateName = factoryClass.aggregateName();
            identifier = factoryClass.simpleName();
        } else {
            aggregateName = aggregateNameForInnerClass(resolvedTypeDeclaration);
            identifier = innerClassQualifiedName(resolvedTypeDeclaration);
        }
        if(typeLevel == 0) {
            modelBuilder.addStandaloneAggregateFactory(new StandaloneAggregateFactory.Builder()
                    .typeComponent(typeComponent(resolvedTypeDeclaration.unresolvedName()))
                    .build());
        }
        container = new MessageListenerContainer.Builder()
                .type(typeLevel == 0 ? MessageListenerContainerType.STANDALONE_FACTORY : MessageListenerContainerType.INNER_FACTORY)
                .aggregateName(aggregateName)
                .containerIdentifier(identifier)
                .build();
    }

    private void visitRepository(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        RepositoryClass repositoryClass = new RepositoryClass(resolvedTypeDeclaration);
        containerLevel = typeLevel;
        String identifier;
        String aggregateName;
        if(typeLevel == 0) {
            aggregateName = repositoryClass.aggregateName();
            identifier = repositoryClass.simpleName();
        } else {
            aggregateName = aggregateNameForInnerClass(resolvedTypeDeclaration);
            identifier = innerClassQualifiedName(resolvedTypeDeclaration);
        }
        if(typeLevel == 0) {
            modelBuilder.addStandaloneAggregateRepository(new StandaloneAggregateRepository.Builder()
                    .typeComponent(typeComponent(resolvedTypeDeclaration.unresolvedName()))
                    .build());
        }
        container = new MessageListenerContainer.Builder()
                .type(typeLevel == 0 ? MessageListenerContainerType.STANDALONE_REPOSITORY : MessageListenerContainerType.INNER_REPOSITORY)
                .aggregateName(aggregateName)
                .containerIdentifier(identifier)
                .build();
    }

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
        aggregateContainerBuilder = new AggregateContainer.Builder();
        aggregateContainerBuilder.typeComponent(typeComponent(resolvedTypeDeclaration.unresolvedName()));
        hooksBuilder = new Hooks.Builder();
    }

    private AggregateContainer.Builder aggregateContainerBuilder;

    @Override
    public void endVisit(ResolvedTypeDeclaration node) {
        if(typeLevel == containerLevel
                && standaloneAggregateRootBuilder != null) {
            modelBuilder.addStandaloneAggregateRoot(standaloneAggregateRootBuilder
                    .hooks(hooksBuilder.build())
                    .build());
            standaloneAggregateRootBuilder = null;
            hooksBuilder = null;
            container = null;
        } else if(typeLevel == 0
                && aggregateContainerBuilder != null) {
            modelBuilder.addAggregateContainer(aggregateContainerBuilder
                    .hooks(hooksBuilder.build())
                    .build());
            aggregateContainerBuilder = null;
            hooksBuilder = null;
            container = null;
        }

        --typeLevel;
    }

    private ModelBuilder modelBuilder = new ModelBuilder();

    private void visitRunner(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var runnerClass = new RunnerClass(resolvedTypeDeclaration);
        modelBuilder.addRunner(new Runner.Builder()
                .withRunnerSource(Optional.of(compilationUnit.sourceFile().source()))
                .withClassName(runnerClass.className())
                .build());
    }

    @Override
    public boolean visit(ResolvedMethod method) {
        if(container != null) {
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
            } else if(container.type().isRoot()) {
                if(method.name().equals(Aggregate.ON_ADD_METHOD_NAME)) {
                    var producedEvents = producesEvents(annotatedMethod);
                    hooksBuilder.onAddProducedEvents(producedEvents);
                    registerMessages(producedEvents, annotatedMethod);
                } else if(method.name().equals(Aggregate.ON_DELETE_METHOD_NAME)) {
                    var producedEvents = producesEvents(annotatedMethod);
                    hooksBuilder.onDeleteProducedEvents(producedEvents);
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

    public Model buildModel() {
        return modelBuilder.build();
    }

    @Override
    public void forget(String sourceId) {
        modelBuilder.forget(sourceId);
    }
}
