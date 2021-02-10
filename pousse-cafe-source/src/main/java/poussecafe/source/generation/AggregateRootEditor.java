package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.analysis.Visibility;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.NormalAnnotationEditor;
import poussecafe.source.generation.tools.TypeDeclarationEditor;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class AggregateRootEditor {

    public void edit() {
        if(typeEditor.isNewType()) {
            compilationUnitEditor.addImport(AggregateRoot.class.getCanonicalName());
            compilationUnitEditor.addImport(poussecafe.domain.EntityAttributes.class.getCanonicalName());

            var modifiers = typeEditor.modifiers();
            modifiers.setVisibility(Visibility.PUBLIC);

            if(aggregate.innerRoot()) {
                modifiers.setStatic(true);
                typeEditor.setName(NamingConventions.innerRootClassName());
            } else {
                compilationUnitEditor.addImport(poussecafe.discovery.Aggregate.class.getCanonicalName());
                compilationUnitEditor.setPackage(aggregate.packageName());

                var annotationEditor = modifiers.normalAnnotation(poussecafe.discovery.Aggregate.class);
                editAggregateAnnotation(annotationEditor.get(0));
                typeEditor.setName(NamingConventions.aggregateRootTypeName(aggregate));
            }

            var aggregateRootSupertype = aggregateRootSupertype();
            typeEditor.setSuperclass(aggregateRootSupertype);

            var attributesType = typeEditor.declaredType(NamingConventions.ATTRIBUTES_CLASS_NAME);
            editAttributesType(attributesType);

            compilationUnitEditor.flush();
        }
    }

    private Aggregate aggregate;

    private void editAggregateAnnotation(NormalAnnotationEditor editor) {
        var factoryClassName = NamingConventions.aggregateFactoryTypeName(aggregate);
        var factoryType = ast.newTypeLiteral(factoryClassName.getIdentifier());
        editor.setAttribute("factory", factoryType);

        var repositoryClassName = NamingConventions.aggregateRepositoryTypeName(aggregate);
        var repositoryType = ast.newTypeLiteral(repositoryClassName.getIdentifier());
        editor.setAttribute("repository", repositoryType);
    }

    private ParameterizedType aggregateRootSupertype() {
        var parametrizedRootType = ast.newParameterizedType(new ClassName(AggregateRoot.class.getSimpleName()));
        parametrizedRootType.typeArguments().add(aggregateIdentifierType());

        var attributesTypeName = NamingConventions.aggregateAttributesQualifiedTypeName(aggregate);
        var attributesType = ast.newSimpleType(attributesTypeName);
        parametrizedRootType.typeArguments().add(attributesType);
        return parametrizedRootType;
    }

    private SimpleType aggregateIdentifierType() {
        return ast.newSimpleType(
                NamingConventions.aggregateIdentifierTypeName(aggregate).getIdentifier());
    }

    private void editAttributesType(TypeDeclarationEditor editor) {
        editor.modifiers().setVisibility(Visibility.PUBLIC);
        editor.modifiers().setStatic(true);
        editor.setInterface(true);
        editor.addSuperinterface(entityAttributesType());
    }

    private ParameterizedType entityAttributesType() {
        ParameterizedType parametrizedType = ast.newParameterizedType(EntityAttributes.class.getSimpleName());
        parametrizedType.typeArguments().add(aggregateIdentifierType());
        return parametrizedType;
    }

    public static class Builder {

        private AggregateRootEditor editor = new AggregateRootEditor();

        public AggregateRootEditor build() {
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

        public Builder typeEditor(TypeDeclarationEditor typeEditor) {
            editor.typeEditor = typeEditor;
            return this;
        }

        public Builder aggregate(Aggregate aggregate) {
            editor.aggregate = aggregate;
            return this;
        }
    }

    private AggregateRootEditor() {

    }

    private CompilationUnitEditor compilationUnitEditor;

    private TypeDeclarationEditor typeEditor;

    private AstWrapper ast;
}
