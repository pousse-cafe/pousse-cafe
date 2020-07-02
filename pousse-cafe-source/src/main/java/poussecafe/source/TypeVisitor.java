package poussecafe.source;

import java.nio.file.Path;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.domain.AggregateRoot;

import static java.util.Objects.requireNonNull;

public class TypeVisitor extends ASTVisitor {

    @Override
    public boolean visit(ImportDeclaration node) {
        imports.tryRegister(node);
        return false;
    }

    private Imports imports = new Imports();

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Path sourcePath;

    @Override
    public boolean visit(TypeDeclaration node) {
        Type type = node.getSuperclassType();
        if(type instanceof ParameterizedType) {
            ParameterizedType parametrizedType = (ParameterizedType) type;
            Type parametrizedTypeType = parametrizedType.getType();
            if(parametrizedTypeType instanceof SimpleType) {
                SimpleType simpleType = (SimpleType) parametrizedTypeType;
                if(imports.resolve(simpleType.getName()).isClass(AggregateRoot.class)) {
                    aggregateRootSourceBuilder = new AggregateRootSource.Builder()
                            .name(node.getName().getIdentifier())
                            .filePath(sourcePath);
                }
            }
            return true;
        } else {
            logger.debug("{} does not contain a parametrized type, skipping", sourcePath);
            return false;
        }
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
        if(MessageListenerSource.isMessageListener(imports, node)) {
            if(aggregateRootSourceBuilder != null) {
                aggregateRootSourceBuilder.withListener(new MessageListenerSource.Builder(imports)
                        .withMethodDeclaration(node)
                        .build());
            }
        }
        return false;
    }

    public static class Builder {

        private TypeVisitor visitor = new TypeVisitor();

        public TypeVisitor build() {
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

    private TypeVisitor() {

    }
}
