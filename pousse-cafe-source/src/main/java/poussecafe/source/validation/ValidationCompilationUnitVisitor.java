package poussecafe.source.validation;

import java.util.Optional;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.source.SourceFile;
import poussecafe.source.analysis.AggregateRootClass;
import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.analysis.CompilationUnitResolver;
import poussecafe.source.analysis.DataAccessImplementationType;
import poussecafe.source.analysis.EntityDefinitionType;
import poussecafe.source.analysis.EntityImplementationType;
import poussecafe.source.analysis.FactoryClass;
import poussecafe.source.analysis.MessageDefinitionType;
import poussecafe.source.analysis.MessageImplementationType;
import poussecafe.source.analysis.MessageListenerMethod;
import poussecafe.source.analysis.RepositoryClass;
import poussecafe.source.analysis.ResolvedMethod;
import poussecafe.source.analysis.ResolvedTypeDeclaration;
import poussecafe.source.analysis.ResolvedTypeName;
import poussecafe.source.analysis.RunnerClass;
import poussecafe.source.analysis.TypeResolvingCompilationUnitVisitor;
import poussecafe.source.analysis.Visibility;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.validation.model.EntityDefinition;
import poussecafe.source.validation.model.EntityImplementation;
import poussecafe.source.validation.model.MessageDefinition;
import poussecafe.source.validation.model.MessageImplementation;
import poussecafe.source.validation.model.MessageListener;
import poussecafe.source.validation.model.Runner;
import poussecafe.source.validation.model.ValidationModel;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

public class ValidationCompilationUnitVisitor extends TypeResolvingCompilationUnitVisitor {

    @Override
    protected boolean visitTypeDeclarationOrSkip(TypeDeclaration node) {
        var resolvedTypeDeclaration = resolve(node);
        if(MessageDefinitionType.isMessageDefinition(resolvedTypeDeclaration)) {
            visitMessageDefinition(resolvedTypeDeclaration);
            return false;
        } else if(MessageImplementationType.isMessageImplementation(resolvedTypeDeclaration)) {
            visitMessageImplementation(resolvedTypeDeclaration);
            return false;
        } else if(EntityDefinitionType.isEntityDefinition(resolvedTypeDeclaration)) {
            visitEntityDefinition(resolvedTypeDeclaration);
            if(AggregateRootClass.isAggregateRoot(resolvedTypeDeclaration)) {
                return true; // Visit listeners
            } else {
                return false;
            }
        } else if(EntityImplementationType.isEntityImplementation(resolvedTypeDeclaration)) {
            visitEntityImplementation(resolvedTypeDeclaration);
            return false;
        } else if(DataAccessImplementationType.isDataAccessImplementation(resolvedTypeDeclaration)) {
            visitDataAccessImplementation(resolvedTypeDeclaration);
            return false;
        } else if(RunnerClass.isRunner(resolvedTypeDeclaration)) {
            visitRunner(resolvedTypeDeclaration);
            return false;
        } else {
            return MessageListenerMethod.isMessageListenerMethodContainer(resolvedTypeDeclaration);
        }
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
        var implementationType = new EntityImplementationType(resolvedTypeDeclaration);
        model.addEntityImplementation(new EntityImplementation.Builder()
                .sourceFileLine(sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .entityImplementationQualifiedClassName(Optional.of(resolvedTypeDeclaration.name().qualifiedName()))
                .entityDefinitionQualifiedClassName(implementationType.entity().map(ResolvedTypeName::qualifiedName))
                .storageNames(implementationType.storageNames())
                .build());
    }

    private void visitDataAccessImplementation(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var implementationType = new DataAccessImplementationType(resolvedTypeDeclaration);
        model.addEntityImplementation(new EntityImplementation.Builder()
                .sourceFileLine(sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .entityImplementationQualifiedClassName(implementationType.dataImplementation().map(ResolvedTypeName::qualifiedName))
                .entityDefinitionQualifiedClassName(implementationType.aggregateRoot().map(ResolvedTypeName::qualifiedName))
                .storageNames(asList(implementationType.storageName()))
                .build());
    }

    private void visitRunner(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var runnerClass = new RunnerClass(resolvedTypeDeclaration);
        model.addRunner(new Runner.Builder()
                .sourceFileLine(sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .classQualifiedName(resolvedTypeDeclaration.name().qualifiedName())
                .typeParametersQualifiedNames(runnerClass.typeParametersQualifiedNames())
                .build());
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        var method = currentResolver().resolve(node);
        if(MessageListenerMethod.isMessageListener(method)) {
            visitMessageListener(method);
        }
        return false;
    }

    private void visitMessageListener(ResolvedMethod method) {
        var messageListenerMethod = new MessageListenerMethod(method);
        model.addMessageListener(new MessageListener.Builder()
                .sourceFileLine(sourceFileLine(method.declaration().getName()))
                .isPublic(method.modifiers().hasVisibility(Visibility.PUBLIC))
                .runnerQualifiedClassName(messageListenerMethod.runner().map(ResolvedTypeName::qualifiedName))
                .returnsValue(messageListenerMethod.returnType().isPresent())
                .consumedMessageQualifiedClassName(messageListenerMethod.consumedMessage().map(ResolvedTypeName::qualifiedName))
                .parametersCount(method.declaration().parameters().size())
                .containerType(messageListenerContainerType(method.declaringType()))
                .build());
    }

    private MessageListenerContainerType messageListenerContainerType(ResolvedTypeDeclaration declaringType) {
        if(AggregateRootClass.isAggregateRoot(declaringType)) {
            return MessageListenerContainerType.ROOT;
        } else if(FactoryClass.isFactory(declaringType)) {
            return MessageListenerContainerType.FACTORY;
        } else if(RepositoryClass.isRepository(declaringType)) {
            return MessageListenerContainerType.REPOSITORY;
        } else {
            return MessageListenerContainerType.OTHER;
        }
    }

    public static class Builder {

        public ValidationCompilationUnitVisitor build() {
            requireNonNull(sourceFile);
            requireNonNull(model);
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
