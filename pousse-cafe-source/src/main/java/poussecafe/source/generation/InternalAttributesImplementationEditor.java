package poussecafe.source.generation;

import java.io.Serializable;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class InternalAttributesImplementationEditor {

    public void edit() {
        compilationUnitEditor.addImportLast(Serializable.class);
        compilationUnitEditor.flush();
    }

    private Aggregate aggregate;

    public static class Builder {

        private InternalAttributesImplementationEditor editor = new InternalAttributesImplementationEditor();

        public InternalAttributesImplementationEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.aggregate);

            editor.ast = new AstWrapper(editor.compilationUnitEditor.ast());

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
    }

    private InternalAttributesImplementationEditor() {

    }

    private ComilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
