package poussecafe.source;

import java.nio.file.Path;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Name;
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
        if(!node.isStatic()) {
            Name importName = node.getName();
            if(!aggregateRootClassImported
                    && importName.isQualifiedName()
                    && importName.getFullyQualifiedName().equals(
                            AggregateRoot.class.getCanonicalName())) {
                logger.debug("Detected import of {} in {}", AggregateRoot.class.getSimpleName(), sourcePath);
                aggregateRootClassImported = true;
            }
        }
        return false;
    }

    private boolean aggregateRootClassImported;

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
                if(extendsAggregateRoot(simpleType)) {
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

    private boolean extendsAggregateRoot(SimpleType simpleType) {
        return extendsAggregateRootWithImport(simpleType)
                || extendsFullyQualifiedAggregateRoot(simpleType);
    }

    private boolean extendsAggregateRootWithImport(SimpleType simpleType) {
        return aggregateRootClassImported
                && simpleType.getName().toString().equals(AggregateRoot.class.getSimpleName());
    }

    private boolean extendsFullyQualifiedAggregateRoot(SimpleType simpleType) {
        return simpleType.getName().isQualifiedName()
                && simpleType.getName().getFullyQualifiedName().equals(AggregateRoot.class.getCanonicalName());
    }

    private AggregateRootSource.Builder aggregateRootSourceBuilder;

    @Override
    public void endVisit(TypeDeclaration node) {
        if(aggregateRootSourceBuilder != null) {
            registry.registerAggregateRoot(aggregateRootSourceBuilder.build());
            aggregateRootSourceBuilder = null;
        }
    }

    private Registry registry;

    public static class Builder {

        private TypeVisitor visitor = new TypeVisitor();

        public TypeVisitor build() {
            requireNonNull(visitor.sourcePath);
            requireNonNull(visitor.registry);
            return visitor;
        }

        public Builder sourcePath(Path sourcePath) {
            visitor.sourcePath = sourcePath;
            return this;
        }

        public Builder registry(Registry registry) {
            visitor.registry = registry;
            return this;
        }
    }

    private TypeVisitor() {

    }
}
