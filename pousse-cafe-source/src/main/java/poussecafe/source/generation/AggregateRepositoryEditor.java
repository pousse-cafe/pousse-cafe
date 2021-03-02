package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.EntityDataAccess;
import poussecafe.source.analysis.Visibility;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.MethodDeclarationEditor;
import poussecafe.source.generation.tools.TypeDeclarationEditor;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class AggregateRepositoryEditor {

    public void edit() {
        if(typeEditor.isNewType()) {
            if(aggregate.innerRepository()) {
                typeEditor.modifiers().setStatic(true);
                typeEditor.setName(NamingConventions.innerRepositoryClassName());
            } else {
                compilationUnitEditor.setPackage(aggregate.packageName());
                typeEditor.setName(NamingConventions.aggregateRepositoryTypeName(aggregate));
            }

            compilationUnitEditor.addImport(AggregateRepository.class.getCanonicalName());

            typeEditor.modifiers().setVisibility(Visibility.PUBLIC);

            var repositoryType = repositoryType();
            typeEditor.setSuperclass(repositoryType);

            addDataAccessMethod(typeEditor.method(NamingConventions.REPOSITORY_DATA_ACCESS_METHOD_NAME).get(0));
            addDataAccess();

            compilationUnitEditor.flush();
        }
    }

    private Aggregate aggregate;

    private ParameterizedType repositoryType() {
        var parametrizedType = ast.newParameterizedType(AggregateRepository.class);

        parametrizedType.typeArguments().add(aggregateIdentifierType());

        SimpleType aggregateRootType = ast.newSimpleType(
                NamingConventions.aggregateRootTypeName(aggregate).getIdentifier());
        parametrizedType.typeArguments().add(aggregateRootType);

        var attributesTypeName = NamingConventions.aggregateAttributesQualifiedTypeName(aggregate);
        var attributesType = ast.newSimpleType(attributesTypeName);
        parametrizedType.typeArguments().add(attributesType);

        return parametrizedType;
    }

    private SimpleType aggregateIdentifierType() {
        return ast.newSimpleType(NamingConventions.aggregateIdentifierTypeName(aggregate).getIdentifier());
    }

    private void addDataAccessMethod(MethodDeclarationEditor editor) {
        if(editor.isNewNode()) {
            editor.modifiers().markerAnnotation(Override.class);
            editor.modifiers().setVisibility(Visibility.PUBLIC);

            var returnType = dataAccessType();
            editor.setReturnType(returnType);

            var body = ast.ast().newBlock();

            var returnStatement = ast.ast().newReturnStatement();
            var castedDataAccess = ast.ast().newCastExpression();
            var parametrizedDataAccessType = dataAccessType();
            castedDataAccess.setType(parametrizedDataAccessType);
            var dataAccessInvocation = ast.ast().newSuperMethodInvocation();
            dataAccessInvocation.setName(ast.ast().newSimpleName("dataAccess"));
            castedDataAccess.setExpression(dataAccessInvocation);
            returnStatement.setExpression(castedDataAccess);
            body.statements().add(returnStatement);

            editor.setBody(body);
        }
    }

    private ParameterizedType dataAccessType() {
        var returnType = ast.newParameterizedType(NamingConventions.innerDataAccessTypeName());
        returnType.typeArguments().add(ast.newSimpleType(NamingConventions.aggregateAttributesQualifiedTypeName(aggregate)));
        return returnType;
    }

    private void addDataAccess() {
        compilationUnitEditor.addImport(EntityDataAccess.class.getCanonicalName());

        var typeName = NamingConventions.innerDataAccessTypeName();
        var dataAccessEditor = typeEditor.newTypeDeclarationLast(typeName);
        dataAccessEditor.modifiers().setVisibility(Visibility.PUBLIC);
        dataAccessEditor.modifiers().setStatic(true);
        dataAccessEditor.setInterface(true);
        dataAccessEditor.setName(typeName);
        dataAccessEditor.setTypeParameter(0, ast.newExtendingTypeParameter("D",
                NamingConventions.aggregateAttributesQualifiedTypeName(aggregate)));

        var entityDataAccessType = entityDataAccessType();
        dataAccessEditor.addSuperinterface(entityDataAccessType);
    }

    private ParameterizedType entityDataAccessType() {
        var parametrizedType = ast.newParameterizedType(EntityDataAccess.class);
        var aggregateIdentifierType = ast.newSimpleType(
                NamingConventions.aggregateIdentifierTypeName(aggregate).getIdentifier());
        parametrizedType.typeArguments().add(aggregateIdentifierType);
        parametrizedType.typeArguments().add(ast.newSimpleType("D"));
        return parametrizedType;
    }

    public static class Builder {

        private AggregateRepositoryEditor editor = new AggregateRepositoryEditor();

        public AggregateRepositoryEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.aggregate);
            requireNonNull(editor.typeEditor);

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

        public Builder typeEditor(TypeDeclarationEditor typeEditor) {
            editor.typeEditor = typeEditor;
            return this;
        }
    }

    private AggregateRepositoryEditor() {

    }

    private CompilationUnitEditor compilationUnitEditor;

    private TypeDeclarationEditor typeEditor;

    private AstWrapper ast;
}
