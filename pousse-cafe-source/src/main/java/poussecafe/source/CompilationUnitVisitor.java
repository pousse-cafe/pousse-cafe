package poussecafe.source;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
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
import poussecafe.source.model.Model;
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

    private Deque<TypeDeclarationResolver> typeDeclarationResolvers = new ArrayDeque<>();

    private CompilationUnit compilationUnit;

    private Path sourcePath;

    @Override
    public boolean visit(TypeDeclaration node) {
        ++typeLevel;
        pushResolver(node);
        return visitListenersContainerOrSkip(node);
    }

    private boolean visitListenersContainerOrSkip(TypeDeclaration node) {
        ResolvedTypeDeclaration resolvedTypeDeclaration = new ResolvedTypeDeclaration.Builder()
                .withResolver(typeDeclarationResolvers.peek())
                .withDeclaration(node)
                .build();
        if(AggregateRootClass.isAggregateRoot(resolvedTypeDeclaration)) {
            AggregateRootClass aggregateRootClass = new AggregateRootClass(resolvedTypeDeclaration);
            createOrUpdateAggregate(aggregateRootClass.aggregateName());
            containerLevel = typeLevel;
            container = new MessageListenerContainer.Builder()
                    .type(MessageListenerContainerType.ROOT)
                    .aggregateName(aggregateRootClass.aggregateName().simpleName())
                    .containerIdentifier(aggregateRootClass.aggregateName().simpleName())
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
            createOrUpdateAggregate(factoryClass.aggregateName());
            containerLevel = typeLevel;
            container = new MessageListenerContainer.Builder()
                    .type(MessageListenerContainerType.FACTORY)
                    .aggregateName(factoryClass.aggregateName().simpleName())
                    .containerIdentifier(factoryClass.simpleName())
                    .build();
            return true;
        } else if(RepositoryClass.isRepository(resolvedTypeDeclaration)) {
            RepositoryClass repositoryClass = new RepositoryClass(resolvedTypeDeclaration);
            createOrUpdateAggregate(repositoryClass.aggregateName());
            containerLevel = typeLevel;
            container = new MessageListenerContainer.Builder()
                    .type(MessageListenerContainerType.REPOSITORY)
                    .aggregateName(repositoryClass.aggregateName().simpleName())
                    .containerIdentifier(repositoryClass.simpleName())
                    .build();
            return true;
        } else if(AggregateContainerClass.isAggregateContainerClass(resolvedTypeDeclaration)) {
            return true;
        } else {
            return false;
        }
    }

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

    private int typeLevel;

    private int containerLevel;

    private void createOrUpdateAggregate(ResolvedTypeName name) {
        aggregateBuilder = new Aggregate.Builder();
        Optional<Aggregate> aggregate = model.aggregate(name.simpleName());
        if(aggregate.isPresent()) {
            aggregateBuilder.startingFrom(aggregate.get());
        } else {
            aggregateBuilder.name(name);
        }
    }

    private Aggregate.Builder aggregateBuilder;

    private MessageListenerContainer container;

    @Override
    public void endVisit(TypeDeclaration node) {
        if(typeLevel == containerLevel
                && aggregateBuilder != null) {
            model.putAggregate(aggregateBuilder.build());
            aggregateBuilder = null;
            container = null;
        }
        typeDeclarationResolvers.pop();
        --typeLevel;
    }

    private Model model;

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

        public Builder model(Model registry) {
            visitor.model = registry;
            return this;
        }
    }

    private CompilationUnitVisitor() {

    }
}
