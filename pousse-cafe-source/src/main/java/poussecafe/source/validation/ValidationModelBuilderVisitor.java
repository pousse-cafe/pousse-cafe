package poussecafe.source.validation;

import java.util.Optional;
import poussecafe.source.analysis.AggregateRootClass;
import poussecafe.source.analysis.DataAccessImplementationType;
import poussecafe.source.analysis.EntityDefinitionType;
import poussecafe.source.analysis.EntityImplementationType;
import poussecafe.source.analysis.FactoryClass;
import poussecafe.source.analysis.MessageDefinitionType;
import poussecafe.source.analysis.MessageImplementationType;
import poussecafe.source.analysis.MessageListenerMethod;
import poussecafe.source.analysis.ModuleClass;
import poussecafe.source.analysis.ProcessDefinitionType;
import poussecafe.source.analysis.RepositoryClass;
import poussecafe.source.analysis.ResolvedCompilationUnit;
import poussecafe.source.analysis.ResolvedCompilationUnitVisitor;
import poussecafe.source.analysis.ResolvedMethod;
import poussecafe.source.analysis.ResolvedTypeDeclaration;
import poussecafe.source.analysis.ResolvedTypeName;
import poussecafe.source.analysis.RunnerClass;
import poussecafe.source.analysis.Visibility;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.model.MessageType;
import poussecafe.source.validation.model.AggregateComponentDefinition;
import poussecafe.source.validation.model.EntityDefinition;
import poussecafe.source.validation.model.EntityImplementation;
import poussecafe.source.validation.model.MessageDefinition;
import poussecafe.source.validation.model.MessageImplementation;
import poussecafe.source.validation.model.MessageListener;
import poussecafe.source.validation.model.Module;
import poussecafe.source.validation.model.ProcessDefinition;
import poussecafe.source.validation.model.Runner;
import poussecafe.source.validation.model.ValidationModel;

import static java.util.Arrays.asList;

public class ValidationModelBuilderVisitor implements ResolvedCompilationUnitVisitor {

    @Override
    public boolean visit(ResolvedCompilationUnit unit) {
        this.unit = unit;
        return true;
    }

    private ResolvedCompilationUnit unit;

    @Override
    public boolean visit(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        if(MessageDefinitionType.isMessageDefinition(resolvedTypeDeclaration)
                || MessageImplementationType.isMessageImplementation(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            if(MessageDefinitionType.isMessageDefinition(resolvedTypeDeclaration)) {
                visitMessageDefinition(resolvedTypeDeclaration);
            }
            if(MessageImplementationType.isMessageImplementation(resolvedTypeDeclaration)) {
                visitMessageImplementation(resolvedTypeDeclaration);
            }
        } else if(EntityDefinitionType.isEntityDefinition(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            visitEntityDefinition(resolvedTypeDeclaration);
            if(AggregateRootClass.isAggregateRoot(resolvedTypeDeclaration)) {
                visitAggregateRootDefinition(resolvedTypeDeclaration);
            }
        } else if(EntityImplementationType.isEntityImplementation(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            visitEntityImplementation(resolvedTypeDeclaration);
        } else if(DataAccessImplementationType.isDataAccessImplementation(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            visitDataAccessImplementation(resolvedTypeDeclaration);
        } else if(RunnerClass.isRunner(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            visitRunner(resolvedTypeDeclaration);
        } else if(ModuleClass.isModule(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            visitModule(resolvedTypeDeclaration);
        } else if(ProcessDefinitionType.isProcessDefinition(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            visitProcessDefinition(resolvedTypeDeclaration);
        } else if(FactoryClass.isFactory(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            visitFactory(resolvedTypeDeclaration);
        } else if(RepositoryClass.isRepository(resolvedTypeDeclaration)) {
            foundPousseCafeComponent(true);
            visitRepository(resolvedTypeDeclaration);
        }
        return MessageListenerMethod.isMessageListenerMethodContainer(resolvedTypeDeclaration);
    }

    @Override
    public boolean foundContent() {
        return foundPousseCafeComponent;
    }

    private boolean foundPousseCafeComponent;

    protected void foundPousseCafeComponent(boolean value) {
        foundPousseCafeComponent = value;
    }

    private void visitMessageDefinition(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var definitionType = new MessageDefinitionType(resolvedTypeDeclaration);
        model.addMessageDefinition(new MessageDefinition.Builder()
                .messageName(definitionType.name())
                .sourceFileLine(unit.sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .qualifiedClassName(resolvedTypeDeclaration.name().qualifiedName())
                .domainEvent(definitionType.type() == MessageType.DOMAIN_EVENT)
                .build());
    }

    private ValidationModel model = new ValidationModel();

    private void visitMessageImplementation(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var implementationType = new MessageImplementationType(resolvedTypeDeclaration);
        model.addMessageImplementation(new MessageImplementation.Builder()
                .sourceFileLine(unit.sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .messageDefinitionClassName(implementationType.messageName().resolvedClass().name())
                .messagingNames(implementationType.messagingNames())
                .className(resolvedTypeDeclaration.unresolvedName().asName())
                .concrete(implementationType.isConcreteImplementation())
                .message(implementationType.implementsMessageInterface())
                .build());
    }

    private void visitEntityDefinition(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var definitionType = new EntityDefinitionType(resolvedTypeDeclaration);
        model.addEntityDefinition(new EntityDefinition.Builder()
                .entityName(definitionType.name())
                .sourceFileLine(unit.sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .className(resolvedTypeDeclaration.unresolvedName().asName())
                .build());
    }

    private void visitAggregateRootDefinition(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var definitionType = new AggregateRootClass(resolvedTypeDeclaration);
        model.addAggregateRootDefinition(new AggregateComponentDefinition.Builder()
                .sourceFileLine(unit.sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .className(resolvedTypeDeclaration.unresolvedName().asName())
                .innerClass(definitionType.isInnerClass())
                .build());
    }

    private void visitEntityImplementation(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var implementationType = new EntityImplementationType(resolvedTypeDeclaration);
        model.addEntityImplementation(new EntityImplementation.Builder()
                .sourceFileLine(unit.sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .entityImplementationQualifiedClassName(Optional.of(resolvedTypeDeclaration.name().qualifiedName()))
                .entityDefinitionQualifiedClassName(implementationType.entity().map(ResolvedTypeName::qualifiedName))
                .storageNames(implementationType.storageNames())
                .build());
    }

    private void visitDataAccessImplementation(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var implementationType = new DataAccessImplementationType(resolvedTypeDeclaration);
        model.addEntityImplementation(new EntityImplementation.Builder()
                .sourceFileLine(unit.sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .entityImplementationQualifiedClassName(implementationType.dataImplementation().map(ResolvedTypeName::qualifiedName))
                .entityDefinitionQualifiedClassName(implementationType.aggregateRoot().map(ResolvedTypeName::qualifiedName))
                .storageNames(asList(implementationType.storageName()))
                .build());
    }

    private void visitRunner(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var runnerClass = new RunnerClass(resolvedTypeDeclaration);
        model.addRunner(new Runner.Builder()
                .sourceFileLine(unit.sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .classQualifiedName(resolvedTypeDeclaration.name().qualifiedName())
                .typeParametersQualifiedNames(runnerClass.typeParametersQualifiedNames())
                .build());
    }

    private void visitModule(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var runnerClass = new ModuleClass(resolvedTypeDeclaration);
        model.addModule(new Module.Builder()
                .sourceFileLine(unit.sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .name(runnerClass.className())
                .build());
    }

    private void visitProcessDefinition(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var processDefinitionClass = new ProcessDefinitionType(resolvedTypeDeclaration);
        model.addProcessDefinition(new ProcessDefinition.Builder()
                .sourceFileLine(unit.sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .className(processDefinitionClass.className())
                .name(processDefinitionClass.processName())
                .build());
    }

    private void visitFactory(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var definitionType = new FactoryClass(resolvedTypeDeclaration);
        model.addAggregateFactory(new AggregateComponentDefinition.Builder()
                .sourceFileLine(unit.sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .className(resolvedTypeDeclaration.unresolvedName().asName())
                .innerClass(definitionType.isInnerClass())
                .build());
    }

    private void visitRepository(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var definitionType = new RepositoryClass(resolvedTypeDeclaration);
        model.addAggregateRepository(new AggregateComponentDefinition.Builder()
                .sourceFileLine(unit.sourceFileLine(resolvedTypeDeclaration.typeDeclaration()))
                .className(resolvedTypeDeclaration.unresolvedName().asName())
                .innerClass(definitionType.isInnerClass())
                .build());
    }

    @Override
    public boolean visit(ResolvedMethod method) {
        if(MessageListenerMethod.isMessageListener(method)) {
            visitMessageListener(method);
        }
        return false;
    }

    private void visitMessageListener(ResolvedMethod method) {
        var messageListenerMethod = new MessageListenerMethod(method);
        model.addMessageListener(new MessageListener.Builder()
                .sourceFileLine(unit.sourceFileLine(method.declaration().getName()))
                .isPublic(method.modifiers().hasVisibility(Visibility.PUBLIC))
                .runnerQualifiedClassName(messageListenerMethod.runner().map(ResolvedTypeName::qualifiedName))
                .returnsValue(messageListenerMethod.returnType().isPresent())
                .consumedMessageClass(messageListenerMethod.consumedMessage().map(ResolvedTypeName::resolvedClass))
                .parametersCount(method.declaration().parameters().size())
                .containerType(messageListenerContainerType(method.declaringType()))
                .build());
    }

    private MessageListenerContainerType messageListenerContainerType(ResolvedTypeDeclaration declaringType) {
        if(AggregateRootClass.isAggregateRoot(declaringType)) {
            return declaringType.isInnerClass() ? MessageListenerContainerType.INNER_ROOT : MessageListenerContainerType.STANDALONE_ROOT;
        } else if(FactoryClass.isFactory(declaringType)) {
            return declaringType.isInnerClass() ? MessageListenerContainerType.INNER_FACTORY : MessageListenerContainerType.STANDALONE_FACTORY;
        } else if(RepositoryClass.isRepository(declaringType)) {
            return declaringType.isInnerClass() ? MessageListenerContainerType.INNER_REPOSITORY : MessageListenerContainerType.STANDALONE_REPOSITORY;
        } else {
            return MessageListenerContainerType.OTHER;
        }
    }

    public ValidationModel buildModel() {
        return model;
    }

    @Override
    public void forget(String sourceId) {
        model.forget(sourceId);
    }
}
