package poussecafe.source.generation;

import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.Command;

import static java.util.Objects.requireNonNull;

public class CommandEditor {

    public void edit() {
        compilationUnitEditor.setPackage(command.packageName());

        compilationUnitEditor.addImport(poussecafe.runtime.Command.class.getCanonicalName());

        var typeEditor = compilationUnitEditor.typeDeclaration();
        typeEditor.modifiers().setVisibility(Visibility.PUBLIC);
        typeEditor.setInterface(true);

        var simpleTypeName = command.simpleName();
        typeEditor.setName(simpleTypeName);

        var valueObjectType = ast.newSimpleType(poussecafe.runtime.Command.class);
        typeEditor.addSuperinterface(valueObjectType);

        compilationUnitEditor.flush();
    }

    private Command command;

    public static class Builder {

        private CommandEditor editor = new CommandEditor();

        public CommandEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.command);

            editor.ast = editor.compilationUnitEditor.ast();

            return editor;
        }

        public Builder compilationUnitEditor(CompilationUnitEditor compilationUnitEditor) {
            editor.compilationUnitEditor = compilationUnitEditor;
            return this;
        }

        public Builder command(Command command) {
            editor.command = command;
            return this;
        }
    }

    private CommandEditor() {

    }

    private CompilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
