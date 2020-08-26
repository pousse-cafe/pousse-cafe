package poussecafe.source.generation;

import java.io.Serializable;
import poussecafe.discovery.MessageImplementation;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.ComilationUnitEditor;
import poussecafe.source.generation.tools.SuppressWarningsEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.DomainEvent;

import static java.util.Objects.requireNonNull;

public class EventImplementationEditor {

    public void edit() {
        var eventImplenentationTypeName = CodeGenerationConventions.eventImplementationTypeName(event);
        compilationUnitEditor.setPackage(eventImplenentationTypeName.getQualifier().toString());

        compilationUnitEditor.addImportLast(Serializable.class.getCanonicalName());
        compilationUnitEditor.addImportLast(MessageImplementation.class.getCanonicalName());
        compilationUnitEditor.addImportLast(event.name());

        var typeEditor = compilationUnitEditor.typeDeclaration();

        var suppressWarningsEditor = new SuppressWarningsEditor(
                typeEditor.modifiers().singleMemberAnnotation(SuppressWarnings.class).get(0));
        suppressWarningsEditor.addWarning("serial");

        var messageImplementationEditor = typeEditor.modifiers().normalAnnotation(
                MessageImplementation.class).get(0);
        var commandDefinitionType = ast.newTypeLiteral(event.name());
        messageImplementationEditor.setAttribute("message", commandDefinitionType);

        typeEditor.modifiers().setVisibility(Visibility.PUBLIC);

        var simpleTypeName = eventImplenentationTypeName.getIdentifier();
        typeEditor.setName(simpleTypeName);

        var serializableType = ast.newSimpleType(Serializable.class);
        typeEditor.addSuperinterface(serializableType);
        typeEditor.addSuperinterface(ast.newSimpleType(event.name().getIdentifier()));

        compilationUnitEditor.flush();
    }

    private DomainEvent event;

    public static class Builder {

        private EventImplementationEditor editor = new EventImplementationEditor();

        public EventImplementationEditor build() {
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

    private EventImplementationEditor() {

    }

    private ComilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
