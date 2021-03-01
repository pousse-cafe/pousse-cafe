package poussecafe.source.generation;

import java.util.Collection;
import java.util.Optional;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.MethodDeclarationEditor;
import poussecafe.source.generation.tools.TypeDeclarationEditor;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.Cardinality;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.SourceModel;

import static java.util.Objects.requireNonNull;

public class AggregateRepositoryMessageListenerEditor extends AggregateMessageListenerEditor {

    @Override
    protected MethodDeclarationEditor insertNewListener(TypeDeclarationEditor typeEditor) {
        return typeEditor.method(messageListener.methodName()).get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setReturnType(MethodDeclarationEditor methodEditor) {
        var identifierTypeName = NamingConventions.aggregateIdentifierTypeName(aggregate).getIdentifier().toString();
        var identifierSimpleType = ast.newSimpleType(identifierTypeName);
        if(messageListener.returnTypeCardinality().isEmpty()
                || messageListener.returnTypeCardinality().orElseThrow() == Cardinality.SINGLE) {
            methodEditor.setReturnType(identifierSimpleType);
        } else if(messageListener.returnTypeCardinality().orElse(null) == Cardinality.OPTIONAL) {
            compilationUnitEditor.addImport(Optional.class);
            var optionalType = ast.newParameterizedType(Optional.class);
            optionalType.typeArguments().add(identifierSimpleType);
            methodEditor.setReturnType(optionalType);
        } else if(messageListener.returnTypeCardinality().orElse(null) == Cardinality.SEVERAL) {
            compilationUnitEditor.addImport(Collection.class);
            var collectionType = ast.newParameterizedType(Collection.class);
            collectionType.typeArguments().add(identifierSimpleType);
            methodEditor.setReturnType(collectionType);
        }
    }

    private Aggregate aggregate;

    @Override
    protected void setBody(MethodDeclarationEditor methodEditor) {
        methodEditor.setEmptyBodyWithComment("TODO: return identifier(s) of aggregates to delete");
    }

    public static class Builder {

        private AggregateRepositoryMessageListenerEditor editor = new AggregateRepositoryMessageListenerEditor();

        public AggregateRepositoryMessageListenerEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.model);
            requireNonNull(editor.messageListener);
            requireNonNull(editor.typeEditor);
            requireNonNull(editor.aggregate);

            editor.ast = editor.compilationUnitEditor.ast();

            return editor;
        }

        public Builder compilationUnitEditor(CompilationUnitEditor compilationUnitEditor) {
            editor.compilationUnitEditor = compilationUnitEditor;
            return this;
        }

        public Builder model(SourceModel model) {
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

        public Builder aggregate(Aggregate aggregate) {
            editor.aggregate = aggregate;
            return this;
        }
    }

    private AggregateRepositoryMessageListenerEditor() {

    }
}
