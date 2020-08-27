package poussecafe.source.generation;

import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.ProcessModel;

import static java.util.Objects.requireNonNull;

public class ProcessEditor {

    public void edit() {
        compilationUnitEditor.setPackage(process.packageName());

        compilationUnitEditor.addImport(poussecafe.domain.Process.class.getCanonicalName());

        var typeEditor = compilationUnitEditor.typeDeclaration();
        typeEditor.modifiers().setVisibility(Visibility.PUBLIC);
        typeEditor.setInterface(true);

        var simpleTypeName = process.simpleName();
        typeEditor.setName(simpleTypeName);

        var processType = ast.newSimpleType(poussecafe.domain.Process.class);
        typeEditor.addSuperinterface(processType);

        compilationUnitEditor.flush();
    }

    private ProcessModel process;

    public static class Builder {

        private ProcessEditor editor = new ProcessEditor();

        public ProcessEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.process);

            editor.ast = editor.compilationUnitEditor.ast();

            return editor;
        }

        public Builder compilationUnitEditor(CompilationUnitEditor compilationUnitEditor) {
            editor.compilationUnitEditor = compilationUnitEditor;
            return this;
        }

        public Builder process(ProcessModel process) {
            editor.process = process;
            return this;
        }
    }

    private ProcessEditor() {

    }

    private CompilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
