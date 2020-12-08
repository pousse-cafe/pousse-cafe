package poussecafe.source.generation;

import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.MethodDeclarationEditor;
import poussecafe.source.generation.tools.TypeDeclarationEditor;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.Model;

import static java.util.Objects.requireNonNull;

public class AggregateRepositoryMessageListenerEditor extends AggregateMessageListenerEditor {

    @Override
    protected MethodDeclarationEditor insertNewListener(TypeDeclarationEditor typeEditor) {
        return typeEditor.method(messageListener.methodName()).get(0);
    }

    @Override
    protected void setBody(MethodDeclarationEditor methodEditor) {
        methodEditor.setEmptyBodyWithComment("TODO: delete aggregate(s)");
    }

    public static class Builder {

        private AggregateRepositoryMessageListenerEditor editor = new AggregateRepositoryMessageListenerEditor();

        public AggregateRepositoryMessageListenerEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.model);
            requireNonNull(editor.messageListener);
            requireNonNull(editor.typeEditor);

            editor.ast = editor.compilationUnitEditor.ast();

            return editor;
        }

        public Builder compilationUnitEditor(CompilationUnitEditor compilationUnitEditor) {
            editor.compilationUnitEditor = compilationUnitEditor;
            return this;
        }

        public Builder model(Model model) {
            editor.model = model;
            return this;
        }

        public Builder messageListener(MessageListener messageListener) {
            editor.messageListener = messageListener;
            return this;
        }

        public Builder typeEditor(TypeDeclarationEditor typeEditor) {
            editor.typeEditor = typeEditor;
            return this;
        }
    }

    private AggregateRepositoryMessageListenerEditor() {

    }
}
