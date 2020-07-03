package poussecafe.source;

import java.nio.file.Path;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.source.analysis.AggregateRootClass;
import poussecafe.source.analysis.FactoryClass;
import poussecafe.source.analysis.MessageListenerAnnotations;
import poussecafe.source.analysis.ResolvedTypeDeclaration;
import poussecafe.source.analysis.ResolvedTypeName;
import poussecafe.source.analysis.Resolver;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageListenerContainer;
import poussecafe.source.model.Model;
import poussecafe.source.model.ProcessModel;

import static java.util.Objects.requireNonNull;

public class CompilationUnitVisitor extends ASTVisitor {

    @Override
    public boolean visit(ImportDeclaration node) {
        resolver.tryRegister(node);
        return false;
    }

    private Resolver resolver = new Resolver();

    private Path sourcePath;

    @Override
    public boolean visit(TypeDeclaration node) {
        ResolvedTypeDeclaration resolvedTypeDeclaration = resolver.resolve(node);
        if(AggregateRootClass.isAggregateRoot(resolvedTypeDeclaration)) {
            AggregateRootClass aggregateRootClass = new AggregateRootClass(resolvedTypeDeclaration);
            createOrUpdateAggregate(aggregateRootClass.aggregateName());
            return true;
        } else if(resolvedTypeDeclaration.implementsInterface(Resolver.PROCESS_INTERFACE)) {
            model.addProcess(new ProcessModel.Builder()
                    .name(resolvedTypeDeclaration.name().simpleName())
                    .filePath(sourcePath)
                    .build());
        } else if(FactoryClass.isFactory(resolvedTypeDeclaration)) {
            FactoryClass factoryClass = new FactoryClass(resolvedTypeDeclaration);
            createOrUpdateAggregate(factoryClass.aggregateName());
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

    @Override
    public void endVisit(TypeDeclaration node) {
        if(aggregateBuilder != null) {
            model.putAggregateRoot(aggregateBuilder.build());
            aggregateBuilder = null;
        }
    }

    private Model model;

    @Override
    public boolean visit(MethodDeclaration node) {
        var method = resolver.resolve(node);
        if(MessageListenerAnnotations.isMessageListener(method.asAnnotatedElement())) {
            if(aggregateBuilder != null) {
                model.addMessageListener(new MessageListener.Builder()
                        .withContainer(MessageListenerContainer.aggregateRoot(aggregateBuilder.name().orElseThrow()))
                        .withMethodDeclaration(resolver.resolve(node))
                        .build());
            }
        }
        return false;
    }

    public static class Builder {

        private CompilationUnitVisitor visitor = new CompilationUnitVisitor();

        public CompilationUnitVisitor build() {
            requireNonNull(visitor.sourcePath);
            requireNonNull(visitor.model);
            return visitor;
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
