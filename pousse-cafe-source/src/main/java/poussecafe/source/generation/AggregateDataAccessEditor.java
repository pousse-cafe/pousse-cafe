package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.ParameterizedType;
import poussecafe.domain.EntityDataAccess;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class AggregateDataAccessEditor {

    public void edit() {
        compilationUnitEditor.setPackage(aggregate.packageName());

        compilationUnitEditor.addImportLast(EntityDataAccess.class.getCanonicalName());

        ParameterizedType entityDataAccessType = entityDataAccessType();

        var typeEditor = compilationUnitEditor.typeDeclaration()
            .setInterface(true)
            .setName(AggregateCodeGenerationConventions.aggregateDataAccessTypeName(aggregate))
            .setTypeParameter(0, ast.newExtendingTypeParameter("D",
                    AggregateCodeGenerationConventions.aggregateAttributesQualifiedTypeName(aggregate)))
            .addSuperinterface(entityDataAccessType);

        typeEditor.modifiers().setVisibility(Visibility.PUBLIC);

        compilationUnitEditor.flush();
    }

    private Aggregate aggregate;

    private ParameterizedType entityDataAccessType() {
        ParameterizedType parametrizedType = ast.newParameterizedType(EntityDataAccess.class);

        var aggregateIdentifierType = ast.newSimpleType(
                AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate).getIdentifier());
        parametrizedType.typeArguments().add(aggregateIdentifierType);

        parametrizedType.typeArguments().add(ast.newSimpleType("D"));

        return parametrizedType;
    }

    public static class Builder {

        private AggregateDataAccessEditor editor = new AggregateDataAccessEditor();

        public AggregateDataAccessEditor build() {
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

    private AggregateDataAccessEditor() {

    }

    private ComilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
