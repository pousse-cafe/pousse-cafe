package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import poussecafe.discovery.DefaultModule;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.source.generation.tools.ComilationUnitEditor;
import poussecafe.source.generation.tools.NormalAnnotationEditor;
import poussecafe.source.generation.tools.TypeDeclarationEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class AggregateRootEditor {

    public void edit() {
        compilationUnitEditor.setPackage(aggregate.packageName());

        compilationUnitEditor.addImportLast(poussecafe.discovery.Aggregate.class.getCanonicalName());
        compilationUnitEditor.addImportLast(poussecafe.discovery.DefaultModule.class.getCanonicalName());
        compilationUnitEditor.addImportLast(AggregateRoot.class.getCanonicalName());
        compilationUnitEditor.addImportLast(poussecafe.domain.EntityAttributes.class.getCanonicalName());

        var typeEditor = compilationUnitEditor.typeDeclaration();

        var modifiers = typeEditor.modifiers();
        var annotationEditor = modifiers.normalAnnotation(poussecafe.discovery.Aggregate.class);
        editAggregateAnnotation(annotationEditor.get(0));

        modifiers.setVisibility(Visibility.PUBLIC);
        typeEditor.setName(aggregate.name());

        var aggregateRootSupertype = aggregateRootSupertype();
        typeEditor.setSuperclass(aggregateRootSupertype);

        var attributesType = typeEditor.declaredType(AggregateCodeGenerationConventions.ATTRIBUTES_CLASS_NAME);
        editAttributesType(attributesType);

        compilationUnitEditor.flush();
    }

    private Aggregate aggregate;

    private void editAggregateAnnotation(NormalAnnotationEditor editor) {
        var factoryClassName = AggregateCodeGenerationConventions.aggregateFactoryTypeName(aggregate);
        var factoryType = ast.newTypeLiteral();
        factoryType.setType(ast.newSimpleType(ast.newSimpleName(factoryClassName.getIdentifier().toString())));
        editor.setAttribute("factory", factoryType);

        var repositoryClassName = AggregateCodeGenerationConventions.aggregateRepositoryTypeName(aggregate);
        var repositoryType = ast.newTypeLiteral();
        repositoryType.setType(ast.newSimpleType(ast.newSimpleName(repositoryClassName.getIdentifier().toString())));
        editor.setAttribute("repository", repositoryType);

        var defaultModuleType = ast.newTypeLiteral();
        defaultModuleType.setType(ast.newSimpleType(ast.newSimpleName(DefaultModule.class.getSimpleName())));
        editor.setAttribute("module", defaultModuleType);
    }

    private ParameterizedType aggregateRootSupertype() {
        var superclassType = ast.newSimpleType(ast.newSimpleName(AggregateRoot.class.getSimpleName()));
        var parametrizedRootType = ast.newParameterizedType(superclassType);

        parametrizedRootType.typeArguments().add(aggregateIdentifierType());

        var aggregateRootTypeName = ast.newSimpleName(aggregate.name());
        var attributesTypeName = ast.newSimpleName(AggregateCodeGenerationConventions.ATTRIBUTES_CLASS_NAME);
        var attributesType = ast.newSimpleType(ast.newQualifiedName(aggregateRootTypeName,
                attributesTypeName));
        parametrizedRootType.typeArguments().add(attributesType);
        return parametrizedRootType;
    }

    private SimpleType aggregateIdentifierType() {
        return ast.newSimpleType(ast.newSimpleName(AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate).getIdentifier().toString()));
    }

    private void editAttributesType(TypeDeclarationEditor editor) {
        editor.modifiers().setVisibility(Visibility.PUBLIC);
        editor.modifiers().setStatic(true);
        editor.setInterface(true);
        editor.addSuperinterface(entityAttributesType());
    }

    private ParameterizedType entityAttributesType() {
        Type simpleType = ast.newSimpleType(ast.newSimpleName(EntityAttributes.class.getSimpleName()));
        ParameterizedType parametrizedType = ast.newParameterizedType(simpleType);
        parametrizedType.typeArguments().add(aggregateIdentifierType());
        return parametrizedType;
    }

    public static class Builder {

        private AggregateRootEditor editor = new AggregateRootEditor();

        public AggregateRootEditor build() {
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

    private AggregateRootEditor() {

    }

    private ComilationUnitEditor compilationUnitEditor;

    private AST ast;
}
