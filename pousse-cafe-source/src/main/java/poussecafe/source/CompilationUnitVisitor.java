package poussecafe.source;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.source.analysis.AggregateContainerClass;
import poussecafe.source.analysis.AggregateRootClass;
import poussecafe.source.analysis.AnnotatedElement;
import poussecafe.source.analysis.CompilationUnitResolver;
import poussecafe.source.analysis.FactoryClass;
import poussecafe.source.analysis.MessageListenerAnnotations;
import poussecafe.source.analysis.ProducedEventAnnotation;
import poussecafe.source.analysis.RepositoryClass;
import poussecafe.source.analysis.ResolutionException;
import poussecafe.source.analysis.ResolvedTypeDeclaration;
import poussecafe.source.analysis.ResolvedTypeName;
import poussecafe.source.analysis.TypeDeclarationResolver;
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

public class CompilationUnitVisitor extends ASTVisitor {

    @Override
    public boolean visit(ImportDeclaration node) {
        resolver.tryRegister(node);
        return false;
    }

    private CompilationUnitResolver resolver;

    private CompilationUnit compilationUnit;

    private Path sourcePath;

    @Override
    public boolean visit(TypeDeclaration node) {
        ++typeLevel;
        pushResolver(node);
        return visitListenersContainerOrSkip(node);
    }

    private int typeLevel = -1;

    private void pushResolver(TypeDeclaration node) {
        TypeDeclarationResolver typeDeclarationResolver;
        if(typeDeclarationResolvers.isEmpty()) {
            typeDeclarationResolver = new TypeDeclarationResolver.Builder()
                    .parent(resolver)
                    .typeDeclaration(node)
                    .containerClass(getRootClass(node))
                    .build();
        } else {
            typeDeclarationResolver = new TypeDeclarationResolver.Builder()
                    .parent(resolver)
                    .typeDeclaration(node)
                    .containerClass(getInnerClass(typeDeclarationResolvers.peek().containerClass(), node))
                    .build();
        }
        typeDeclarationResolvers.push(typeDeclarationResolver);
    }

    private Deque<TypeDeclarationResolver> typeDeclarationResolvers = new ArrayDeque<>();

    private Class<?> getRootClass(TypeDeclaration typeDeclaration) {
        var className = compilationUnit.getPackage().getName().getFullyQualifiedName()
                + "."
                + typeDeclaration.getName().getFullyQualifiedName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ResolutionException("Unable to resolve root class " + className);
        }
    }

    private Class<?> getInnerClass(Class<?> containerClass, TypeDeclaration node) {
        var innerClassName = node.getName().getFullyQualifiedName();
        return getDeclaredClass(containerClass, innerClassName);
    }

    private Class<?> getDeclaredClass(Class<?> containerClass, String innerClassName) {
        return Arrays.stream(containerClass.getDeclaredClasses())
                .filter(innerClass -> innerClass.getSimpleName().equals(innerClassName))
                .findFirst().orElseThrow();
    }

    private boolean visitListenersContainerOrSkip(TypeDeclaration node) {
        ResolvedTypeDeclaration resolvedTypeDeclaration = new ResolvedTypeDeclaration.Builder()
                .withResolver(typeDeclarationResolvers.peek())
                .withDeclaration(node)
                .build();
        if(AggregateRootClass.isAggregateRoot(resolvedTypeDeclaration)) {
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
            createOrUpdateAggregate(aggregateName);
            container = new MessageListenerContainer.Builder()
                    .type(MessageListenerContainerType.ROOT)
                    .aggregateName(aggregateName)
                    .containerIdentifier(identifier)
                    .build();
            return true;
        } else if(resolvedTypeDeclaration.implementsInterface(CompilationUnitResolver.PROCESS_INTERFACE)) {
            model.addProcess(new ProcessModel.Builder()
                    .name(resolvedTypeDeclaration.name().simpleName())
                    .packageName(compilationUnit.getPackage().getName().getFullyQualifiedName())
                    .build());
            return false;
        } else if(FactoryClass.isFactory(resolvedTypeDeclaration)) {
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
            createOrUpdateAggregate(aggregateName);
            container = new MessageListenerContainer.Builder()
                    .type(MessageListenerContainerType.FACTORY)
                    .aggregateName(aggregateName)
                    .containerIdentifier(identifier)
                    .build();
            return true;
        } else if(RepositoryClass.isRepository(resolvedTypeDeclaration)) {
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
            createOrUpdateAggregate(aggregateName);
            container = new MessageListenerContainer.Builder()
                    .type(MessageListenerContainerType.REPOSITORY)
                    .aggregateName(aggregateName)
                    .containerIdentifier(identifier)
                    .build();
            return true;
        } else {
            return AggregateContainerClass.isAggregateContainerClass(resolvedTypeDeclaration);
        }
    }

    private void createOrUpdateAggregate(String name) {
        aggregateBuilder = model.getAndCreateIfAbsent(name,
                compilationUnit.getPackage().getName().getFullyQualifiedName());
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
    public void endVisit(TypeDeclaration node) {
        if(typeLevel == containerLevel
                && aggregateBuilder != null) {
            aggregateBuilder = null;
            container = null;
        }
        typeDeclarationResolvers.pop();
        --typeLevel;
    }

    private ModelBuilder model;

    @Override
    public boolean visit(MethodDeclaration node) {
        if(aggregateBuilder != null) {
            var method = typeDeclarationResolvers.peek().resolve(node);
            var annotatedMethod = method.asAnnotatedElement();
            if(MessageListenerAnnotations.isMessageListener(annotatedMethod)) {
                var messageListener = new MessageListener.Builder()
                        .withContainer(container)
                        .withMethodDeclaration(typeDeclarationResolvers.peek().resolve(node))
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

        private CompilationUnitVisitor visitor = new CompilationUnitVisitor();

        public CompilationUnitVisitor build() {
            requireNonNull(visitor.compilationUnit);
            requireNonNull(visitor.sourcePath);
            requireNonNull(visitor.model);

            visitor.resolver = new CompilationUnitResolver(visitor.compilationUnit);

            return visitor;
        }

        public Builder compilationUnit(CompilationUnit compilationUnit) {
            visitor.compilationUnit = compilationUnit;
            return this;
        }

        public Builder sourcePath(Path sourcePath) {
            visitor.sourcePath = sourcePath;
            return this;
        }

        public Builder model(ModelBuilder registry) {
            visitor.model = registry;
            return this;
        }
    }

    private CompilationUnitVisitor() {

    }
}
