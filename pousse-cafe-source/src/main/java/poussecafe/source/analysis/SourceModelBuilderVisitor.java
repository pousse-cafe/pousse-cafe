package poussecafe.source.analysis;

import java.io.Serializable;
import java.util.List;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.source.WithPersistableState;
import poussecafe.source.model.AggregateContainer;
import poussecafe.source.model.Command;
import poussecafe.source.model.DomainEvent;
import poussecafe.source.model.Message;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageListenerContainer;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.model.MessageType;
import poussecafe.source.model.ProcessModel;
import poussecafe.source.model.ProducedEvent;
import poussecafe.source.model.Runner;
import poussecafe.source.model.SourceModel;
import poussecafe.source.model.SourceModelBuilder;
import poussecafe.source.model.StandaloneAggregateFactory;
import poussecafe.source.model.StandaloneAggregateRepository;
import poussecafe.source.model.StandaloneAggregateRoot;
import poussecafe.source.model.TypeComponent;

import static java.util.stream.Collectors.toList;

public class SourceModelBuilderVisitor implements ResolvedCompilationUnitVisitor, WithPersistableState {

    @Override
    public boolean visit(ResolvedCompilationUnit unit) {
        compilationUnit = unit;
        return false;
    }

    private int typeLevel = -1;

    private ResolvedCompilationUnit compilationUnit;

    @Override
    public boolean visit(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        ++typeLevel;
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
        containerLevel = typeLevel;
        String identifier;
        String aggregateName;
        if(typeLevel == 0) {
            aggregateName = aggregateRootClass.aggregateName();
            identifier = resolvedTypeDeclaration.name().simpleName();
            createStandaloneAggregateRoot(resolvedTypeDeclaration);
        } else {
            aggregateName = aggregateNameForInnerClass(resolvedTypeDeclaration);
            identifier = innerClassQualifiedName(resolvedTypeDeclaration);
        }
        container = new MessageListenerContainer.Builder()
                .type(typeLevel == 0 ? MessageListenerContainerType.STANDALONE_ROOT : MessageListenerContainerType.INNER_ROOT)
                .aggregateName(aggregateName)
                .containerIdentifier(identifier)
                .build();
    }

    private void createStandaloneAggregateRoot(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        modelBuilder.addStandaloneAggregateRoot(new StandaloneAggregateRoot.Builder()
                .typeComponent(typeComponent(resolvedTypeDeclaration.unresolvedName()))
                .build());
    }

    private TypeComponent typeComponent(SafeClassName typeName) {
        return new TypeComponent.Builder()
                .source(compilationUnit.sourceFile())
                .name(typeName)
                .build();
    }

    private void visitProcessDefinition(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var processDefinition = new ProcessDefinitionType(resolvedTypeDeclaration);
        modelBuilder.addProcess(new ProcessModel.Builder()
                .name(processDefinition.processName())
                .packageName(compilationUnit.packageName())
                .source(compilationUnit.sourceFile())
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
        modelBuilder.addAggregateContainer(new AggregateContainer.Builder()
                .typeComponent(typeComponent(resolvedTypeDeclaration.unresolvedName()))
                .build());
    }

    @Override
    public void endVisit(ResolvedTypeDeclaration node) {
        if(typeLevel == containerLevel) {
            container = null;
        } else if(typeLevel == 0) {
            container = null;
        }
        --typeLevel;
    }

    private SourceModelBuilder modelBuilder = new SourceModelBuilder();

    private void visitRunner(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var runnerClass = new RunnerClass(resolvedTypeDeclaration);
        modelBuilder.addRunner(new Runner.Builder()
                .withRunnerSource(compilationUnit.sourceFile())
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
                        .withSource(compilationUnit.sourceFile())
                        .withMethodDeclaration(listenerMethod)
                        .build();
                modelBuilder.addMessageListener(messageListener);

                registerMessage(messageListener.consumedMessage(),
                        method.parameterTypeName(0).orElseThrow());
                registerMessages(messageListener.producedEvents(), annotatedMethod);
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

    public SourceModel buildModel() {
        return modelBuilder.build();
    }

    @Override
    public void forget(String sourceId) {
        modelBuilder.forget(sourceId);
    }

    @Override
    public Serializable getSerializableState() {
        return modelBuilder;
    }

    @Override
    public void loadSerializedState(Serializable state) {
        modelBuilder = (SourceModelBuilder) state;
    }
}
