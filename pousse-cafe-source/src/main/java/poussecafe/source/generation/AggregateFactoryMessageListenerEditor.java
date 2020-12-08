package poussecafe.source.generation;

import java.util.Collection;
import java.util.Optional;
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
        var aggregateRootSimpleTypeName = NamingConventions.aggregateRootTypeName(aggregate).getIdentifier().toString();
        if(messageListener.productionType().isEmpty()
                || messageListener.productionType().orElseThrow() == ProductionType.SINGLE) {
            methodEditor.setReturnType(ast.newSimpleType(aggregateRootSimpleTypeName));
        } else if(messageListener.productionType().orElse(null) == ProductionType.OPTIONAL) {
            compilationUnitEditor.addImport(Optional.class);
            var optionalType = ast.newParameterizedType(Optional.class);
            optionalType.typeArguments().add(ast.newSimpleType(aggregateRootSimpleTypeName));
            methodEditor.setReturnType(optionalType);
        } else if(messageListener.productionType().orElse(null) == ProductionType.SEVERAL) {
            compilationUnitEditor.addImport(Collection.class);
            var collectionType = ast.newParameterizedType(Collection.class);
            collectionType.typeArguments().add(ast.newSimpleType(aggregateRootSimpleTypeName));
            methodEditor.setReturnType(collectionType);
        }
    }

    private Aggregate aggregate;

    @Override
    protected void setBody(MethodDeclarationEditor methodEditor) {
        if(messageListener.productionType().orElseThrow() == ProductionType.SINGLE) {
            methodEditor.setEmptyBodyWithComment("TODO: build aggregate");
        } else if(messageListener.productionType().orElseThrow() == ProductionType.OPTIONAL) {
            methodEditor.setEmptyBodyWithComment("TODO: build optional aggregate");
        } else if(messageListener.productionType().orElseThrow() == ProductionType.SEVERAL) {
            methodEditor.setEmptyBodyWithComment("TODO: build aggregate(s)");
        }
        methodEditor.appendStatementToBody(ast.newReturnNullStatement());
    }

    public static class Builder {

        private AggregateFactoryMessageListenerEditor editor = new AggregateFactoryMessageListenerEditor();

        public AggregateFactoryMessageListenerEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.model);
            requireNonNull(editor.messageListener);
            requireNonNull(editor.aggregate);
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

        public Builder aggregate(Aggregate aggregate) {
            editor.aggregate = aggregate;
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

    private AggregateFactoryMessageListenerEditor() {

    }
}
