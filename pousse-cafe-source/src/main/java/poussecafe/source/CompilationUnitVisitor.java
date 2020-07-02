package poussecafe.source;

import java.nio.file.Path;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.domain.AggregateRoot;
import poussecafe.source.model.AggregateRootSource;
import poussecafe.source.model.MessageListenerContainer;
import poussecafe.source.model.MessageListenerSource;
import poussecafe.source.model.Model;
import poussecafe.source.model.ProcessModel;
import poussecafe.source.resolution.Imports;
import poussecafe.source.resolution.ResolvedTypeDeclaration;
import poussecafe.source.resolution.ResolvedTypeName;

import static java.util.Objects.requireNonNull;

public class CompilationUnitVisitor extends ASTVisitor {

    @Override
    public boolean visit(ImportDeclaration node) {
        imports.tryRegister(node);
        return false;
    }

    private Imports imports = new Imports();

    private Path sourcePath;

    @Override
    public boolean visit(TypeDeclaration node) {
        ResolvedTypeDeclaration resolvedTypeDeclaration = imports.resolve(node);
        Optional<ResolvedTypeName> superclassType = resolvedTypeDeclaration.superclass();
        if(superclassType.isPresent()
                && superclassType.get().isClass(AggregateRoot.class)) {
            aggregateRootSourceBuilder = new AggregateRootSource.Builder()
                    .name(node.getName().getIdentifier())
                    .filePath(sourcePath);
            return true;
        } else if(resolvedTypeDeclaration.implementsInterface(poussecafe.domain.Process.class)) {
            model.addProcess(new ProcessModel.Builder()
                    .name(resolvedTypeDeclaration.name().simpleName())
                    .filePath(sourcePath)
                    .build());
        }
        return false;
    }

    private AggregateRootSource.Builder aggregateRootSourceBuilder;

    @Override
    public void endVisit(TypeDeclaration node) {
        if(aggregateRootSourceBuilder != null) {
            model.addAggregateRoot(aggregateRootSourceBuilder.build());
            aggregateRootSourceBuilder = null;
        }
    }

    private Model model;

    @Override
    public boolean visit(MethodDeclaration node) {
        var method = imports.resolve(node);
        if(MessageListenerSource.isMessageListener(method.asAnnotatedElement())) {
            if(aggregateRootSourceBuilder != null) {
                model.addMessageListener(new MessageListenerSource.Builder()
                        .withContainer(MessageListenerContainer.aggregateRoot(aggregateRootSourceBuilder.name().orElseThrow()))
                        .withMethodDeclaration(imports.resolve(node))
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
