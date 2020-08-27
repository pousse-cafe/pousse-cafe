package poussecafe.source.generation.internal;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.source.generation.NamingConventions;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.Aggregate;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class InternalDataAccessImplementationEditor {

    public void edit() {
        compilationUnitEditor.setPackage(NamingConventions.adaptersPackageName(aggregate));

        compilationUnitEditor.addImport(DataAccessImplementation.class);
        compilationUnitEditor.addImport(NamingConventions.aggregateRootTypeName(aggregate));
        compilationUnitEditor.addImport(NamingConventions.aggregateDataAccessTypeName(aggregate));
        compilationUnitEditor.addImport(NamingConventions.aggregateIdentifierTypeName(aggregate));
        compilationUnitEditor.addImport(InternalDataAccess.class);
        compilationUnitEditor.addImport(InternalStorage.class);

        var typeEditor = compilationUnitEditor.typeDeclaration();

        var dataAccessImplementationAnnotation = typeEditor.modifiers().normalAnnotation(DataAccessImplementation.class).get(0);
        dataAccessImplementationAnnotation.setAttribute("aggregateRoot", ast.newTypeLiteral(NamingConventions.aggregateRootTypeName(aggregate).getIdentifier()));
        dataAccessImplementationAnnotation.setAttribute("dataImplementation", ast.newTypeLiteral(NamingConventions.aggregateAttributesImplementationTypeName(aggregate).getIdentifier()));

        var storageNameAccess = ast.ast().newFieldAccess();
        storageNameAccess.setExpression(ast.ast().newSimpleName(InternalStorage.class.getSimpleName()));
        storageNameAccess.setName(ast.ast().newSimpleName("NAME"));
        dataAccessImplementationAnnotation.setAttribute("storageName", storageNameAccess);

        typeEditor.modifiers().setVisibility(Visibility.PUBLIC);
        var typeName = NamingConventions.aggregateDataAccessImplementationTypeName(aggregate, InternalStorageAdaptersCodeGenerator.INTERNAL_STORAGE_NAME).getIdentifier();
        typeEditor.setName(typeName);

        var superclassType = ast.newParameterizedType(InternalDataAccess.class);
        superclassType.typeArguments().add(ast.newSimpleType(NamingConventions.aggregateIdentifierTypeName(aggregate).getIdentifier()));
        superclassType.typeArguments().add(ast.newSimpleType(NamingConventions.aggregateAttributesImplementationTypeName(aggregate).getIdentifier()));
        typeEditor.setSuperclass(superclassType);

        var superinterfaceType = ast.newParameterizedType(NamingConventions.aggregateDataAccessTypeName(aggregate).getIdentifier());
        superinterfaceType.typeArguments().add(ast.newSimpleType(NamingConventions.aggregateAttributesImplementationTypeName(aggregate).getIdentifier()));
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

        public Builder compilationUnitEditor(CompilationUnitEditor compilationUnitEditor) {
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

    private CompilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
