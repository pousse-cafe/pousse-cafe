package poussecafe.source.generation;

import java.io.Serializable;
import poussecafe.discovery.MessageImplementation;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.ComilationUnitEditor;
import poussecafe.source.generation.tools.SuppressWarningsEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.Command;

import static java.util.Objects.requireNonNull;

public class CommandImplementationEditor {

    public void edit() {
        var commandImplenentationTypeName = CodeGenerationConventions.commandImplementationTypeName(command);
        compilationUnitEditor.setPackage(commandImplenentationTypeName.getQualifier().toString());

        compilationUnitEditor.addImportLast(Serializable.class.getCanonicalName());
        compilationUnitEditor.addImportLast(MessageImplementation.class.getCanonicalName());
        compilationUnitEditor.addImportLast(command.name());

        var typeEditor = compilationUnitEditor.typeDeclaration();

        var suppressWarningsEditor = new SuppressWarningsEditor(
                typeEditor.modifiers().singleMemberAnnotation(SuppressWarnings.class).get(0));
        suppressWarningsEditor.addWarning("serial");

        var messageImplementationEditor = typeEditor.modifiers().normalAnnotation(
                MessageImplementation.class).get(0);
        var commandDefinitionType = ast.newTypeLiteral(command.name());
        messageImplementationEditor.setAttribute("message", commandDefinitionType);

        typeEditor.modifiers().setVisibility(Visibility.PUBLIC);

        var simpleTypeName = commandImplenentationTypeName.getIdentifier();
        typeEditor.setName(simpleTypeName);

        var serializableType = ast.newSimpleType(Serializable.class);
        typeEditor.addSuperinterface(serializableType);
        typeEditor.addSuperinterface(ast.newSimpleType(command.name().getIdentifier()));

        compilationUnitEditor.flush();
    }

    private Command command;

    public static class Builder {

        private CommandImplementationEditor editor = new CommandImplementationEditor();

        public CommandImplementationEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.command);

            editor.ast = editor.compilationUnitEditor.ast();

            return editor;
        }

        public Builder compilationUnitEditor(ComilationUnitEditor compilationUnitEditor) {
            editor.compilationUnitEditor = compilationUnitEditor;
            return this;
        }

        public Builder command(Command command) {
            editor.command = command;
            return this;
        }
    }

    private CommandImplementationEditor() {

    }

    private ComilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
