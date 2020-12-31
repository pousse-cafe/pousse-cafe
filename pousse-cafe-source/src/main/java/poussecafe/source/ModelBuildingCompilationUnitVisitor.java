package poussecafe.source;

import java.util.List;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.source.analysis.AggregateContainerClass;
import poussecafe.source.analysis.AggregateRootClass;
import poussecafe.source.analysis.AnnotatedElement;
import poussecafe.source.analysis.CompilationUnitResolver;
import poussecafe.source.analysis.FactoryClass;
import poussecafe.source.analysis.MessageListenerMethod;
import poussecafe.source.analysis.ProcessDefinitionType;
import poussecafe.source.analysis.ProducedEventAnnotation;
import poussecafe.source.analysis.RepositoryClass;
import poussecafe.source.analysis.ResolvedTypeDeclaration;
import poussecafe.source.analysis.ResolvedTypeName;
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
        } else {
            return AggregateContainerClass.isAggregateContainerClass(resolvedTypeDeclaration);
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
        container = new MessageListenerContainer.Builder()
                .type(MessageListenerContainerType.ROOT)
                .aggregateName(aggregateName)
                .containerIdentifier(identifier)
                .build();
    }

    private void visitProcessDefinition(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var processDefinition = new ProcessDefinitionType(resolvedTypeDeclaration);
        model.addProcess(new ProcessModel.Builder()
                .name(processDefinition.processName())
                .packageName(compilationUnit().getPackage().getName().getFullyQualifiedName())
                .build());
    }

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
        container = new MessageListenerContainer.Builder()
                .type(MessageListenerContainerType.REPOSITORY)
                .aggregateName(aggregateName)
                .containerIdentifier(identifier)
                .build();
    }

    private void createOrUpdateAggregate(String name) {
        aggregateBuilder = model.getAndCreateIfAbsent(name,
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

    @Override
    protected void endTypeDeclarationVisit(TypeDeclaration node) {
        if(typeLevel() == containerLevel
                && aggregateBuilder != null) {
            aggregateBuilder = null;
            container = null;
        }
    }

    private ModelBuilder model;

    @Override
    public boolean visit(MethodDeclaration node) {
        if(aggregateBuilder != null) {
            var method = currentResolver().resolve(node);
            var annotatedMethod = method.asAnnotatedElement();
            if(MessageListenerMethod.isMessageListener(method)) {
                var messageListener = new MessageListener.Builder()
                        .withContainer(container)
                        .withMethodDeclaration(new MessageListenerMethod(method))
                        .build();
                model.addMessageListener(messageListener);

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
            model.addCommand(new Command.Builder()
                    .name(message.name())
                    .packageName(messageTypeName.packageName())
                    .build());
        } else if(message.type() == MessageType.DOMAIN_EVENT) {
            model.addEvent(new DomainEvent.Builder()
                    .name(message.name())
                    .packageName(messageTypeName.packageName())
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
            requireNonNull(model);

            var visitor = new ModelBuildingCompilationUnitVisitor(compilationUnit);
            visitor.model = model;
            return visitor;
        }

        public Builder compilationUnitResolver(CompilationUnitResolver compilationUnit) {
            this.compilationUnit = compilationUnit;
            return this;
        }

        private CompilationUnitResolver compilationUnit;

        public Builder model(ModelBuilder model) {
            this.model = model;
            return this;
        }

        private ModelBuilder model;
    }

    private ModelBuildingCompilationUnitVisitor(CompilationUnitResolver compilationUnit) {
        super(compilationUnit);
    }
}
