package poussecafe.source.generation;

import java.util.Collection;
import java.util.Optional;
import poussecafe.discovery.ProducesEvent;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.MethodDeclarationEditor;
import poussecafe.source.generation.tools.TypeDeclarationEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.DomainEvent;
import poussecafe.source.model.Message;
import poussecafe.source.model.Model;
import poussecafe.source.model.ProducedEvent;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class AggregateRootHooksEditor {

    public void edit() {
        var typeEditor = compilationUnitEditor.typeDeclaration();

        importProducedEvents(aggregate.onAddProducedEvents());
        importProducedEvents(aggregate.onDeleteProducedEvents());

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
                .map(name -> model.event(name))
                .map(Optional::get)
                .collect(toList());
        for(DomainEvent event : events) {
            compilationUnitEditor.addImport(event.name());
        }
    }

    private Model model;

    private Aggregate aggregate;

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

            if(!producedEvents.isEmpty()) {
                compilationUnitEditor.addImport(ProducesEvent.class);
            }
        }

        var producesEventEditor = new ProducesEventsEditor.Builder()
            .methodEditor(methodEditor)
            .producedEvents(producedEvents)
            .build();
        producesEventEditor.edit();
        methodEditor.modifiers().markerAnnotation(Override.class);
        methodEditor.modifiers().setVisibility(Visibility.PUBLIC);

        if(methodEditor.isNewNode()) {
            if(!producedEvents.isEmpty()) {
                methodEditor.setEmptyBodyWithComment("TODO: issue expected events");
            } else {
                methodEditor.setEmptyBody();
            }
        }
    }

    private void editOnDeleteMethod(TypeDeclarationEditor editor) {
        editHook(editor, "onDelete", aggregate.onDeleteProducedEvents());
    }

    public static class Builder {

        private AggregateRootHooksEditor editor = new AggregateRootHooksEditor();

        public AggregateRootHooksEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.aggregate);
            requireNonNull(editor.model);
            return editor;
        }

        public Builder compilationUnitEditor(CompilationUnitEditor compilationUnitEditor) {
            editor.compilationUnitEditor = compilationUnitEditor;
            return this;
        }

        public Builder aggregate(Aggregate aggregate) {
            editor.aggregate = aggregate;
            return this;
        }

        public Builder model(Model model) {
            editor.model = model;
            return this;
        }
    }

    private AggregateRootHooksEditor() {

    }

    private CompilationUnitEditor compilationUnitEditor;
}
