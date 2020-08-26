package poussecafe.source.generation;

import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.ComilationUnitEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.DomainEvent;

import static java.util.Objects.requireNonNull;

public class EventEditor {

    public void edit() {
        compilationUnitEditor.setPackage(event.packageName());

        compilationUnitEditor.addImportLast(poussecafe.domain.DomainEvent.class.getCanonicalName());

        var typeEditor = compilationUnitEditor.typeDeclaration();
        typeEditor.modifiers().setVisibility(Visibility.PUBLIC);
        typeEditor.setInterface(true);

        var simpleTypeName = event.simpleName();
        typeEditor.setName(simpleTypeName);

        var valueObjectType = ast.newSimpleType(poussecafe.domain.DomainEvent.class);
        typeEditor.addSuperinterface(valueObjectType);

        compilationUnitEditor.flush();
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

        public Builder compilationUnitEditor(ComilationUnitEditor compilationUnitEditor) {
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

    private ComilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
