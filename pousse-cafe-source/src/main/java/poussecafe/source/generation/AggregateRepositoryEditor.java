package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import poussecafe.domain.Repository;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class AggregateRepositoryEditor {

    public void edit() {
        compilationUnitEditor.setPackage(aggregate.packageName());

        compilationUnitEditor.addImportLast(Repository.class.getCanonicalName());

        ParameterizedType supertype = supertype();

        var typeEditor = compilationUnitEditor.typeDeclaration()
            .setName(AggregateCodeGenerationConventions.aggregateRepositoryTypeName(aggregate))
            .setSuperclass(supertype);

        dataAccessMethod(typeEditor.method(AggregateCodeGenerationConventions.REPOSITORY_DATA_ACCESS_METHOD_NAME).get(0));

        typeEditor.modifiers().setVisibility(Visibility.PUBLIC);

        compilationUnitEditor.flush();
    }

    private Aggregate aggregate;

    private ParameterizedType supertype() {
        var parametrizedType = ast.newParameterizedType(Repository.class);

        SimpleType aggregateRootType = ast.newSimpleType(
                AggregateCodeGenerationConventions.aggregateRootTypeName(aggregate).getIdentifier());
        parametrizedType.typeArguments().add(aggregateRootType);

        parametrizedType.typeArguments().add(aggregateIdentifierType());

        var attributesTypeName = AggregateCodeGenerationConventions.aggregateAttributesQualifiedTypeName(aggregate);
        var attributesType = ast.newSimpleType(attributesTypeName);
        parametrizedType.typeArguments().add(attributesType);

        return parametrizedType;
    }

    private SimpleType aggregateIdentifierType() {
        return ast.newSimpleType(AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate).getIdentifier());
    }

    private void dataAccessMethod(MethodDeclarationEditor editor) {
        editor.modifiers().markerAnnotation(Override.class);
        editor.modifiers().setVisibility(Visibility.PUBLIC);

        var returnType = ast.newParameterizedType(AggregateCodeGenerationConventions.aggregateDataAccessTypeName(aggregate).getIdentifier());
        returnType.typeArguments().add(ast.newSimpleType(AggregateCodeGenerationConventions.aggregateAttributesQualifiedTypeName(aggregate)));
        editor.setReturnType(returnType);

        var body = ast.ast().newBlock();

        var returnStatement = ast.ast().newReturnStatement();
        var castedDataAccess = ast.ast().newCastExpression();
        var parametrizedDataAccessType = ast.newParameterizedType(
                AggregateCodeGenerationConventions.aggregateDataAccessTypeName(aggregate).getIdentifier());
        parametrizedDataAccessType.typeArguments().add(ast.newSimpleType(AggregateCodeGenerationConventions.aggregateAttributesQualifiedTypeName(aggregate)));
        castedDataAccess.setType(parametrizedDataAccessType);
        var dataAccessInvocation = ast.ast().newSuperMethodInvocation();
        dataAccessInvocation.setName(ast.ast().newSimpleName("dataAccess"));
        castedDataAccess.setExpression(dataAccessInvocation);
        returnStatement.setExpression(castedDataAccess);
        body.statements().add(returnStatement);

        editor.setBody(body);
    }

    public static class Builder {

        private AggregateRepositoryEditor editor = new AggregateRepositoryEditor();

        public AggregateRepositoryEditor build() {
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

    private AggregateRepositoryEditor() {

    }

    private ComilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
