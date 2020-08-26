package poussecafe.source.generation.internal;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.source.generation.AggregateCodeGenerationConventions;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.ComilationUnitEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.Aggregate;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class InternalDataAccessImplementationEditor {

    public void edit() {
        compilationUnitEditor.setPackage(AggregateCodeGenerationConventions.adaptersPackageName(aggregate));

        compilationUnitEditor.addImportLast(DataAccessImplementation.class);
        compilationUnitEditor.addImportLast(AggregateCodeGenerationConventions.aggregateRootTypeName(aggregate));
        compilationUnitEditor.addImportLast(AggregateCodeGenerationConventions.aggregateDataAccessTypeName(aggregate));
        compilationUnitEditor.addImportLast(AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate));
        compilationUnitEditor.addImportLast(InternalDataAccess.class);
        compilationUnitEditor.addImportLast(InternalStorage.class);

        var typeEditor = compilationUnitEditor.typeDeclaration();

        var dataAccessImplementationAnnotation = typeEditor.modifiers().normalAnnotation(DataAccessImplementation.class).get(0);
        dataAccessImplementationAnnotation.setAttribute("aggregateRoot", ast.newTypeLiteral(AggregateCodeGenerationConventions.aggregateRootTypeName(aggregate).getIdentifier()));
        dataAccessImplementationAnnotation.setAttribute("dataImplementation", ast.newTypeLiteral(AggregateCodeGenerationConventions.aggregateAttributesImplementationTypeName(aggregate).getIdentifier()));

        var storageNameAccess = ast.ast().newFieldAccess();
        storageNameAccess.setExpression(ast.ast().newSimpleName(InternalStorage.class.getSimpleName()));
        storageNameAccess.setName(ast.ast().newSimpleName("NAME"));
        dataAccessImplementationAnnotation.setAttribute("storageName", storageNameAccess);

        typeEditor.modifiers().setVisibility(Visibility.PUBLIC);
        var typeName = AggregateCodeGenerationConventions.aggregateDataAccessImplementationTypeName(aggregate, InternalStorageAdaptersCodeGenerator.INTERNAL_STORAGE_NAME).getIdentifier();
        typeEditor.setName(typeName);

        var superclassType = ast.newParameterizedType(InternalDataAccess.class);
        superclassType.typeArguments().add(ast.newSimpleType(AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate).getIdentifier()));
        superclassType.typeArguments().add(ast.newSimpleType(AggregateCodeGenerationConventions.aggregateAttributesImplementationTypeName(aggregate).getIdentifier()));
        typeEditor.setSuperclass(superclassType);

        var superinterfaceType = ast.newParameterizedType(AggregateCodeGenerationConventions.aggregateDataAccessTypeName(aggregate).getIdentifier());
        superinterfaceType.typeArguments().add(ast.newSimpleType(AggregateCodeGenerationConventions.aggregateAttributesImplementationTypeName(aggregate).getIdentifier()));
        typeEditor.addSuperinterface(superinterfaceType);

        var constructorEditor = typeEditor.constructors(typeName.getIdentifier().toString()).get(0);
        constructorEditor.modifiers().setVisibility(Visibility.PUBLIC);

        if(constructorEditor.isNewNode()) {
            var constructorBody = ast.ast().newBlock();
            var versionFieldSet = ast.ast().newMethodInvocation();
            versionFieldSet.setName(ast.ast().newSimpleName("versionField"));
            var versionFieldName = ast.newStringLiteral("version");
            versionFieldSet.arguments().add(versionFieldName);
            constructorBody.statements().add(ast.ast().newExpressionStatement(versionFieldSet));
            constructorEditor.setBody(constructorBody);
        }

        compilationUnitEditor.flush();
    }

    private Aggregate aggregate;

    public static class Builder {

        private InternalDataAccessImplementationEditor editor = new InternalDataAccessImplementationEditor();

        public InternalDataAccessImplementationEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.aggregate);

            editor.ast = editor.compilationUnitEditor.ast();

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

    private InternalDataAccessImplementationEditor() {

    }

    private ComilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
