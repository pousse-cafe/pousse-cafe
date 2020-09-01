package poussecafe.source.generation;

import java.util.Collection;
import java.util.Optional;
import org.eclipse.jdt.core.dom.Block;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.MethodDeclarationEditor;
import poussecafe.source.generation.tools.TypeDeclarationEditor;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.Model;
import poussecafe.source.model.ProductionType;

import static java.util.Objects.requireNonNull;

public class AggregateFactoryMessageListenerEditor extends AggregateMessageListenerEditor {

    @Override
    protected MethodDeclarationEditor insertNewListener(TypeDeclarationEditor typeEditor) {
        return typeEditor.method(messageListener.methodName()).get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setReturnType(MethodDeclarationEditor methodEditor) {
        if(messageListener.productionType().isEmpty()
                || messageListener.productionType().orElseThrow() == ProductionType.SINGLE) {
            methodEditor.setReturnType(ast.newSimpleType(aggregate.simpleName()));
        } else if(messageListener.productionType().orElse(null) == ProductionType.OPTIONAL) {
            compilationUnitEditor.addImport(Optional.class);
            var optionalType = ast.newParameterizedType(Optional.class);
            optionalType.typeArguments().add(ast.newSimpleType(aggregate.simpleName()));
            methodEditor.setReturnType(optionalType);
        } else if(messageListener.productionType().orElse(null) == ProductionType.SEVERAL) {
            compilationUnitEditor.addImport(Collection.class);
            var collectionType = ast.newParameterizedType(Collection.class);
            collectionType.typeArguments().add(ast.newSimpleType(aggregate.simpleName()));
            methodEditor.setReturnType(collectionType);
        }
    }

    private Aggregate aggregate;

    @SuppressWarnings("unchecked")
    @Override
    protected Block newListenerBody() {
        var block = ast.ast().newBlock();
        block.statements().add(ast.newReturnNullStatement());
        return block;
    }

    public static class Builder {

        private AggregateFactoryMessageListenerEditor editor = new AggregateFactoryMessageListenerEditor();

        public AggregateFactoryMessageListenerEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.model);
            requireNonNull(editor.messageListener);
            requireNonNull(editor.aggregate);

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

        public Builder aggregate(Aggregate aggregate) {
            editor.aggregate = aggregate;
            return this;
        }

        public Builder messageListener(MessageListener messageListener) {
            editor.messageListener = messageListener;
            return this;
        }
    }

    private AggregateFactoryMessageListenerEditor() {

    }
}
