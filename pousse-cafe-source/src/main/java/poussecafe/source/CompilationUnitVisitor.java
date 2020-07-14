package poussecafe.source;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.source.analysis.AggregateRootClass;
import poussecafe.source.analysis.AnnotatedElement;
import poussecafe.source.analysis.FactoryClass;
import poussecafe.source.analysis.MessageListenerAnnotations;
import poussecafe.source.analysis.ProducedEventAnnotation;
import poussecafe.source.analysis.RepositoryClass;
import poussecafe.source.analysis.ResolvedTypeDeclaration;
import poussecafe.source.analysis.ResolvedTypeName;
import poussecafe.source.analysis.Resolver;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageListenerContainer;
import poussecafe.source.model.MessageListenerContainerType;
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

    private Resolver resolver;

    private CompilationUnit compilationUnit;

    private Path sourcePath;

    @Override
    public boolean visit(TypeDeclaration node) {
        ResolvedTypeDeclaration resolvedTypeDeclaration = resolver.resolve(node);
        if(AggregateRootClass.isAggregateRoot(resolvedTypeDeclaration)) {
            AggregateRootClass aggregateRootClass = new AggregateRootClass(resolvedTypeDeclaration);
            createOrUpdateAggregate(aggregateRootClass.aggregateName());
            container = new MessageListenerContainer.Builder()
                    .type(MessageListenerContainerType.ROOT)
                    .aggregateName(aggregateRootClass.aggregateName().simpleName())
                    .className(aggregateRootClass.aggregateName().simpleName())
                    .build();
            return true;
        } else if(resolvedTypeDeclaration.implementsInterface(Resolver.PROCESS_INTERFACE)) {
            model.addProcess(new ProcessModel.Builder()
                    .name(resolvedTypeDeclaration.name().simpleName())
                    .filePath(sourcePath)
                    .build());
        } else if(FactoryClass.isFactory(resolvedTypeDeclaration)) {
            FactoryClass factoryClass = new FactoryClass(resolvedTypeDeclaration);
            createOrUpdateAggregate(factoryClass.aggregateName());
            container = new MessageListenerContainer.Builder()
                    .type(MessageListenerContainerType.FACTORY)
                    .aggregateName(factoryClass.aggregateName().simpleName())
                    .className(factoryClass.simpleName())
                    .build();
            return true;
        } else if(RepositoryClass.isRepository(resolvedTypeDeclaration)) {
            RepositoryClass repositoryClass = new RepositoryClass(resolvedTypeDeclaration);
            createOrUpdateAggregate(repositoryClass.aggregateName());
            container = new MessageListenerContainer.Builder()
                    .type(MessageListenerContainerType.REPOSITORY)
                    .aggregateName(repositoryClass.aggregateName().simpleName())
                    .className(repositoryClass.simpleName())
                    .build();
            return true;
        }
        return false;
    }

    private void createOrUpdateAggregate(ResolvedTypeName name) {
        aggregateBuilder = new Aggregate.Builder();
        Optional<Aggregate> aggregate = model.aggregateRoot(name.simpleName());
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
        if(aggregateBuilder != null) {
            model.putAggregateRoot(aggregateBuilder.build());
            aggregateBuilder = null;
            container = null;
        }
    }

    private Model model;

    @Override
    public boolean visit(MethodDeclaration node) {
        if(aggregateBuilder != null) {
            var method = resolver.resolve(node);
            if(MessageListenerAnnotations.isMessageListener(method.asAnnotatedElement())) {
                model.addMessageListener(new MessageListener.Builder()
                        .withContainer(container)
                        .withMethodDeclaration(resolver.resolve(node))
                        .build());
            } else if(container.type() == MessageListenerContainerType.ROOT) {
                if(method.name().equals(Aggregate.ON_ADD_METHOD_NAME)) {
                    var annotatedMethod = method.asAnnotatedElement();
                    var producedEvents = producesEvents(annotatedMethod);
                    aggregateBuilder.onAddProducedEvents(producedEvents);
                } else if(method.name().equals(Aggregate.ON_DELETE_METHOD_NAME)) {
                    var annotatedMethod = method.asAnnotatedElement();
                    var producedEvents = producesEvents(annotatedMethod);
                    aggregateBuilder.onDeleteProducedEvents(producedEvents);
                } else if(method.name().equals(Aggregate.ON_UPDATE_METHOD_NAME)) {
                    var annotatedMethod = method.asAnnotatedElement();
                    var producedEvents = producesEvents(annotatedMethod);
                    aggregateBuilder.onUpdateProducedEvents(producedEvents);
                }
            }
        }
        return false;
    }

    private List<ProducedEvent> producesEvents(AnnotatedElement<MethodDeclaration> method) {
        return method.findAnnotations(Resolver.PRODUCES_EVENT_ANNOTATION_CLASS)
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

            visitor.resolver = new Resolver(visitor.compilationUnit);

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
