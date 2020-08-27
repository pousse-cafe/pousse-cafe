package poussecafe.source.generation;

import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.DomainEvent;

import static java.util.Objects.requireNonNull;

public class EventEditor {

    public void edit() {
        if(compilationUnitEditor.isNew()) {
            compilationUnitEditor.setPackage(event.packageName());

            compilationUnitEditor.addImport(poussecafe.domain.DomainEvent.class.getCanonicalName());

            var typeEditor = compilationUnitEditor.typeDeclaration();
            typeEditor.modifiers().setVisibility(Visibility.PUBLIC);
            typeEditor.setInterface(true);

            var simpleTypeName = event.simpleName();
            typeEditor.setName(simpleTypeName);

            var valueObjectType = ast.newSimpleType(poussecafe.domain.DomainEvent.class);
            typeEditor.addSuperinterface(valueObjectType);

            compilationUnitEditor.flush();
        }
    }

    private DomainEvent event;

    public static class Builder {

        private EventEditor editor = new EventEditor();

        public EventEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.event);

            editor.ast = editor.compilationUnitEditor.ast();

            return editor;
        }

        public Builder compilationUnitEditor(CompilationUnitEditor compilationUnitEditor) {
            editor.compilationUnitEditor = compilationUnitEditor;
            return this;
        }

        public Builder event(DomainEvent event) {
            editor.event = event;
            return this;
        }
    }

    private EventEditor() {

    }

    private CompilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
