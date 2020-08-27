package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import poussecafe.domain.Repository;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.MethodDeclarationEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class AggregateRepositoryEditor {

    public void edit() {
        if(compilationUnitEditor.isNew()) {
            compilationUnitEditor.setPackage(aggregate.packageName());

            compilationUnitEditor.addImport(Repository.class.getCanonicalName());

            var typeEditor = compilationUnitEditor.typeDeclaration();
            typeEditor.modifiers().setVisibility(Visibility.PUBLIC);
            typeEditor.setName(NamingConventions.aggregateRepositoryTypeName(aggregate));

            var repositoryType = repositoryType();
            typeEditor.setSuperclass(repositoryType);

            editDataAccessMethod(typeEditor.method(NamingConventions.REPOSITORY_DATA_ACCESS_METHOD_NAME).get(0));

            compilationUnitEditor.flush();
        }
    }

    private Aggregate aggregate;

    private ParameterizedType repositoryType() {
        var parametrizedType = ast.newParameterizedType(Repository.class);

        SimpleType aggregateRootType = ast.newSimpleType(
                NamingConventions.aggregateRootTypeName(aggregate).getIdentifier());
        parametrizedType.typeArguments().add(aggregateRootType);

        parametrizedType.typeArguments().add(aggregateIdentifierType());

        var attributesTypeName = NamingConventions.aggregateAttributesQualifiedTypeName(aggregate);
        var attributesType = ast.newSimpleType(attributesTypeName);
        parametrizedType.typeArguments().add(attributesType);

        return parametrizedType;
    }

    private SimpleType aggregateIdentifierType() {
        return ast.newSimpleType(NamingConventions.aggregateIdentifierTypeName(aggregate).getIdentifier());
    }

    private void editDataAccessMethod(MethodDeclarationEditor editor) {
        if(editor.isNewNode()) {
            editor.modifiers().markerAnnotation(Override.class);
            editor.modifiers().setVisibility(Visibility.PUBLIC);

            var returnType = ast.newParameterizedType(NamingConventions.aggregateDataAccessTypeName(aggregate).getIdentifier());
            returnType.typeArguments().add(ast.newSimpleType(NamingConventions.aggregateAttributesQualifiedTypeName(aggregate)));
            editor.setReturnType(returnType);

            var body = ast.ast().newBlock();

            var returnStatement = ast.ast().newReturnStatement();
            var castedDataAccess = ast.ast().newCastExpression();
            var parametrizedDataAccessType = ast.newParameterizedType(
                    NamingConventions.aggregateDataAccessTypeName(aggregate).getIdentifier());
            parametrizedDataAccessType.typeArguments().add(ast.newSimpleType(NamingConventions.aggregateAttributesQualifiedTypeName(aggregate)));
            castedDataAccess.setType(parametrizedDataAccessType);
            var dataAccessInvocation = ast.ast().newSuperMethodInvocation();
            dataAccessInvocation.setName(ast.ast().newSimpleName("dataAccess"));
            castedDataAccess.setExpression(dataAccessInvocation);
            returnStatement.setExpression(castedDataAccess);
            body.statements().add(returnStatement);

            editor.setBody(body);
        }
    }

    public static class Builder {

        private AggregateRepositoryEditor editor = new AggregateRepositoryEditor();

        public AggregateRepositoryEditor build() {
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

    private AggregateRepositoryEditor() {

    }

    private CompilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
