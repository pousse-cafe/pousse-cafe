package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import poussecafe.domain.Factory;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.ComilationUnitEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class AggregateFactoryEditor {

    public void edit() {
        compilationUnitEditor.setPackage(aggregate.packageName());

        compilationUnitEditor.addImportLast(Factory.class.getCanonicalName());

        var typeEditor = compilationUnitEditor.typeDeclaration();
        typeEditor.modifiers().setVisibility(Visibility.PUBLIC);
        typeEditor.setName(AggregateCodeGenerationConventions.aggregateFactoryTypeName(aggregate));

        var factorySupertype = factorySupertype();
        typeEditor.setSuperclass(factorySupertype);

        compilationUnitEditor.flush();
    }

    private Aggregate aggregate;

    private ParameterizedType factorySupertype() {
        var parametrizedType = ast.newParameterizedType(Factory.class);

        parametrizedType.typeArguments().add(aggregateIdentifierType());

        var aggregateRootType = ast.newSimpleType(
                AggregateCodeGenerationConventions.aggregateRootTypeName(aggregate).getIdentifier());
        parametrizedType.typeArguments().add(aggregateRootType);

        var attributesType = ast.newSimpleType(AggregateCodeGenerationConventions.aggregateAttributesQualifiedTypeName(aggregate));
        parametrizedType.typeArguments().add(attributesType);

        return parametrizedType;
    }

    private SimpleType aggregateIdentifierType() {
        return ast.newSimpleType(AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate).getIdentifier());
    }

    public static class Builder {

        private AggregateFactoryEditor editor = new AggregateFactoryEditor();

        public AggregateFactoryEditor build() {
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

    private AggregateFactoryEditor() {

    }

    private ComilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
