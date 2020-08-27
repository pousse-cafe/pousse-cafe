package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.ParameterizedType;
import poussecafe.domain.EntityDataAccess;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class AggregateDataAccessEditor {

    public void edit() {
        if(compilationUnitEditor.isNew()) {
            compilationUnitEditor.setPackage(aggregate.packageName());

            compilationUnitEditor.addImport(EntityDataAccess.class.getCanonicalName());

            var typeEditor = compilationUnitEditor.typeDeclaration();
            typeEditor.modifiers().setVisibility(Visibility.PUBLIC);
            typeEditor.setInterface(true);
            typeEditor.setName(NamingConventions.aggregateDataAccessTypeName(aggregate));
            typeEditor.setTypeParameter(0, ast.newExtendingTypeParameter("D",
                    NamingConventions.aggregateAttributesQualifiedTypeName(aggregate)));

            var entityDataAccessType = entityDataAccessType();
            typeEditor.addSuperinterface(entityDataAccessType);

            compilationUnitEditor.flush();
        }
    }

    private Aggregate aggregate;

    private ParameterizedType entityDataAccessType() {
        var parametrizedType = ast.newParameterizedType(EntityDataAccess.class);

        var aggregateIdentifierType = ast.newSimpleType(
                NamingConventions.aggregateIdentifierTypeName(aggregate).getIdentifier());
        parametrizedType.typeArguments().add(aggregateIdentifierType);

        parametrizedType.typeArguments().add(ast.newSimpleType("D"));

        return parametrizedType;
    }

    public static class Builder {

        private AggregateDataAccessEditor editor = new AggregateDataAccessEditor();

        public AggregateDataAccessEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.aggregate);

            editor.ast = editor.compilationUnitEditor.ast();

            return editor;
        }

        public Builder compilationUnitEditor(CompilationUnitEditor compilationUnitEditor) {
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

    private CompilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
