package poussecafe.source.generation;

import poussecafe.source.analysis.Name;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.MethodDeclarationEditor;
import poussecafe.source.generation.tools.NormalAnnotationEditor;
import poussecafe.source.generation.tools.TypeDeclarationEditor;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.Model;

import static java.util.Objects.requireNonNull;

public class AggregateRootMessageListenerEditor extends AggregateMessageListenerEditor {

    @Override
    protected MethodDeclarationEditor insertNewListener(TypeDeclarationEditor typeEditor) {
        MethodDeclarationEditor methodEditor;
        var attributesType = typeEditor.findTypeDeclarationByName(
                NamingConventions.ATTRIBUTES_CLASS_NAME).orElseThrow();
        methodEditor = typeEditor.insertNewMethodBefore(attributesType);
        return methodEditor;
    }

    @Override
    protected void setListenerRunner(NormalAnnotationEditor messageListenerAnnotationEditor) {
        var runnerType = ast.newTypeLiteral(new Name(messageListener.runnerName().orElseThrow()));
        messageListenerAnnotationEditor.setAttribute("runner", runnerType);
    }

    public static class Builder {

        private AggregateRootMessageListenerEditor editor = new AggregateRootMessageListenerEditor();

        public AggregateRootMessageListenerEditor build() {
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

    private AggregateRootMessageListenerEditor() {

    }
}
