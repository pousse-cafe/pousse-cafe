package poussecafe.source.validation;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.source.SourceFile;
import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.analysis.CompilationUnitResolver;
import poussecafe.source.analysis.EntityDefinitionType;
import poussecafe.source.analysis.EntityImplementationType;
import poussecafe.source.analysis.MessageDefinitionType;
import poussecafe.source.analysis.MessageImplementationType;
import poussecafe.source.analysis.ResolvedTypeDeclaration;
import poussecafe.source.analysis.ResolvedTypeName;
import poussecafe.source.analysis.TypeResolvingCompilationUnitVisitor;
import poussecafe.source.validation.model.EntityDefinition;
import poussecafe.source.validation.model.EntityImplementation;
import poussecafe.source.validation.model.MessageDefinition;
import poussecafe.source.validation.model.MessageImplementation;
import poussecafe.source.validation.model.ValidationModel;

import static java.util.Objects.requireNonNull;

public class ValidationCompilationUnitVisitor extends TypeResolvingCompilationUnitVisitor {

    @Override
    protected boolean visitTypeDeclarationOrSkip(TypeDeclaration node) {
        var resolvedTypeDeclaration = resolve(node);
        if(MessageDefinitionType.isMessageDefinition(resolvedTypeDeclaration)) {
            visitMessageDefinition(resolvedTypeDeclaration);
        } else if(MessageImplementationType.isMessageImplementation(resolvedTypeDeclaration)) {
            visitMessageImplementation(resolvedTypeDeclaration);
        } else if(EntityDefinitionType.isEntityDefinition(resolvedTypeDeclaration)) {
            visitEntityDefinition(resolvedTypeDeclaration);
        } else if(EntityImplementationType.isEntityImplementation(resolvedTypeDeclaration)) {
            visitEntityImplementation(resolvedTypeDeclaration);
        }
        return false;
    }

    private void visitMessageDefinition(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var definitionType = new MessageDefinitionType(resolvedTypeDeclaration);
        model.addMessageDefinition(new MessageDefinition.Builder()
                .messageName(definitionType.name())
                .sourceFileLine(sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .qualifiedClassName(resolvedTypeDeclaration.name().qualifiedName())
                .build());
    }

    private ValidationModel model;

    private SourceFileLine sourceFileLine(ASTNode node) {
        return new SourceFileLine.Builder()
                .sourceFile(sourceFile)
                .line(lineNumber(node))
                .build();
    }

    private SourceFile sourceFile;

    private void visitMessageImplementation(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var definitionType = new MessageImplementationType(resolvedTypeDeclaration);
        model.addMessageImplementation(new MessageImplementation.Builder()
                .sourceFileLine(sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .messageDefinitionQualifiedClassName(definitionType.messageName().map(ResolvedTypeName::qualifiedName))
                .messagingNames(definitionType.messagingNames())
                .build());
    }

    private void visitEntityDefinition(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var definitionType = new EntityDefinitionType(resolvedTypeDeclaration);
        model.addEntityDefinition(new EntityDefinition.Builder()
                .entityName(definitionType.name())
                .sourceFileLine(sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .qualifiedClassName(resolvedTypeDeclaration.name().qualifiedName())
                .build());
    }

    private void visitEntityImplementation(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var definitionType = new EntityImplementationType(resolvedTypeDeclaration);
        model.addEntityImplementation(new EntityImplementation.Builder()
                .sourceFileLine(sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .entityDefinitionQualifiedClassName(definitionType.entity().map(ResolvedTypeName::qualifiedName))
                .storageNames(definitionType.storageNames())
                .build());
    }

    public static class Builder {

        public ValidationCompilationUnitVisitor build() {
            requireNonNull(model);
            requireNonNull(sourceFile);
            requireNonNull(classResolver);

            var visitor = new ValidationCompilationUnitVisitor(new CompilationUnitResolver.Builder()
                    .classResolver(classResolver)
                    .compilationUnit(sourceFile.tree())
                    .build());
            visitor.model = model;
            visitor.sourceFile = sourceFile;
            return visitor;
        }

        public Builder sourceFile(SourceFile sourceFile) {
            this.sourceFile = sourceFile;
            return this;
        }

        private SourceFile sourceFile;

        public Builder model(ValidationModel model) {
            this.model = model;
            return this;
        }

        private ValidationModel model;

        public Builder classResolver(ClassResolver classResolver) {
            this.classResolver = classResolver;
            return this;
        }

        private ClassResolver classResolver;
    }

    private ValidationCompilationUnitVisitor(CompilationUnitResolver compilationUnit) {
        super(compilationUnit);
    }
}
