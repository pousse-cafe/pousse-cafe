package poussecafe.source.generation;

import java.util.Collection;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import poussecafe.discovery.DefaultModule;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.source.analysis.Name;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.ComilationUnitEditor;
import poussecafe.source.generation.tools.MethodDeclarationEditor;
import poussecafe.source.generation.tools.NormalAnnotationEditor;
import poussecafe.source.generation.tools.TypeDeclarationEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.DomainEvent;
import poussecafe.source.model.Message;
import poussecafe.source.model.Model;
import poussecafe.source.model.ProducedEvent;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("unchecked")
public class AggregateRootEditor {

    public void edit() {
        compilationUnitEditor.setPackage(aggregate.packageName());

        compilationUnitEditor.addImportLast(poussecafe.discovery.Aggregate.class.getCanonicalName());
        compilationUnitEditor.addImportLast(poussecafe.discovery.DefaultModule.class.getCanonicalName());
        compilationUnitEditor.addImportLast(AggregateRoot.class.getCanonicalName());
        compilationUnitEditor.addImportLast(poussecafe.domain.EntityAttributes.class.getCanonicalName());

        importProducedEvents(aggregate.onAddProducedEvents());
        importProducedEvents(aggregate.onDeleteProducedEvents());

        var typeEditor = compilationUnitEditor.typeDeclaration();

        var modifiers = typeEditor.modifiers();
        var annotationEditor = modifiers.normalAnnotation(poussecafe.discovery.Aggregate.class);
        editAggregateAnnotation(annotationEditor.get(0));

        modifiers.setVisibility(Visibility.PUBLIC);
        typeEditor.setName(aggregate.name());

        var aggregateRootSupertype = aggregateRootSupertype();
        typeEditor.setSuperclass(aggregateRootSupertype);

        var attributesType = typeEditor.declaredType(AggregateCodeGenerationConventions.ATTRIBUTES_CLASS_NAME);
        editAttributesType(attributesType);

        if(!aggregate.onDeleteProducedEvents().isEmpty()) {
            editOnDeleteMethod(typeEditor);
        }

        if(!aggregate.onAddProducedEvents().isEmpty()) {
            editOnAddMethod(typeEditor);
        }

        compilationUnitEditor.flush();
    }

    private void importProducedEvents(Collection<ProducedEvent> producedEvents) {
        var events = producedEvents.stream()
                .map(ProducedEvent::message)
                .map(Message::name)
                .map(name -> model.orElseThrow().event(name))
                .map(Optional::get)
                .collect(toList());
        for(DomainEvent event : events) {
            compilationUnitEditor.addImportFirst(event.name());
        }
    }

    private Optional<Model> model;

    private Aggregate aggregate;

    private void editAggregateAnnotation(NormalAnnotationEditor editor) {
        var factoryClassName = AggregateCodeGenerationConventions.aggregateFactoryTypeName(aggregate);
        var factoryType = ast.newTypeLiteral(factoryClassName.getIdentifier());
        editor.setAttribute("factory", factoryType);

        var repositoryClassName = AggregateCodeGenerationConventions.aggregateRepositoryTypeName(aggregate);
        var repositoryType = ast.newTypeLiteral(repositoryClassName.getIdentifier());
        editor.setAttribute("repository", repositoryType);

        var defaultModuleType = ast.newTypeLiteral(new Name(DefaultModule.class.getSimpleName()));
        editor.setAttribute("module", defaultModuleType);
    }

    private ParameterizedType aggregateRootSupertype() {
        var parametrizedRootType = ast.newParameterizedType(new Name(AggregateRoot.class.getSimpleName()));
        parametrizedRootType.typeArguments().add(aggregateIdentifierType());

        var attributesTypeName = AggregateCodeGenerationConventions.aggregateAttributesQualifiedTypeName(aggregate);
        var attributesType = ast.newSimpleType(attributesTypeName);
        parametrizedRootType.typeArguments().add(attributesType);
        return parametrizedRootType;
    }

    private SimpleType aggregateIdentifierType() {
        return ast.newSimpleType(
                AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate).getIdentifier());
    }

    private void editAttributesType(TypeDeclarationEditor editor) {
        editor.modifiers().setVisibility(Visibility.PUBLIC);
        editor.modifiers().setStatic(true);
        editor.setInterface(true);
        editor.addSuperinterface(entityAttributesType());
    }

    private ParameterizedType entityAttributesType() {
        ParameterizedType parametrizedType = ast.newParameterizedType(EntityAttributes.class.getSimpleName());
        parametrizedType.typeArguments().add(aggregateIdentifierType());
        return parametrizedType;
    }

    private void editOnAddMethod(TypeDeclarationEditor editor) {
        editHook(editor, "onAdd", aggregate.onAddProducedEvents());
    }

    private void editHook(TypeDeclarationEditor editor, String hookName, Collection<ProducedEvent> producedEvents) {
        var methods = editor.findMethods(hookName);

        MethodDeclarationEditor methodEditor;
        if(!methods.isEmpty()) {
            methodEditor = editor.editMethod(methods.get(0), false);
        } else {
            methodEditor = editor.insertNewMethodFirst();
            methodEditor.setName(hookName);
        }

        var producesEventEditor = new ProducesEventsEditor.Builder()
            .methodEditor(methodEditor)
            .producedEvents(producedEvents)
            .build();
        producesEventEditor.edit();
        methodEditor.modifiers().markerAnnotation(Override.class);
        methodEditor.modifiers().setVisibility(Visibility.PUBLIC);

        if(methodEditor.isNewNode()) {
            methodEditor.setBody(ast.ast().newBlock());
        }
    }

    private void editOnDeleteMethod(TypeDeclarationEditor editor) {
        editHook(editor, "onDelete", aggregate.onDeleteProducedEvents());
    }

    public static class Builder {

        private AggregateRootEditor editor = new AggregateRootEditor();

        public AggregateRootEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.aggregate);
            requireNonNull(editor.model);

            editor.ast = editor.compilationUnitEditor.ast();

            return editor;
        }

        public Builder compilationUnitEditor(ComilationUnitEditor compilationUnitEditor) {
            editor.compilationUnitEditor = compilationUnitEditor;
            return this;
        }

        public Builder aggregate(Aggregate aggregate) {
            editor.aggregate = aggregate;
            return this;
        }

        public Builder model(Optional<Model> model) {
            editor.model = model;
            return this;
        }
    }

    private AggregateRootEditor() {

    }

    private ComilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
