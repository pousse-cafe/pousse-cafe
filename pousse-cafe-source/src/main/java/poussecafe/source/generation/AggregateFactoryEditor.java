package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import poussecafe.domain.Factory;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class AggregateFactoryEditor {

    public void edit() {
        compilationUnitEditor.setPackage(aggregate.packageName());

        compilationUnitEditor.addImport(Factory.class.getCanonicalName());

        var typeEditor = compilationUnitEditor.typeDeclaration();
        typeEditor.modifiers().setVisibility(Visibility.PUBLIC);
        typeEditor.setName(NamingConventions.aggregateFactoryTypeName(aggregate));

        var factorySupertype = factorySupertype();
        typeEditor.setSuperclass(factorySupertype);

        compilationUnitEditor.flush();
    }

    private Aggregate aggregate;

    private ParameterizedType factorySupertype() {
        var parametrizedType = ast.newParameterizedType(Factory.class);

        parametrizedType.typeArguments().add(aggregateIdentifierType());

        var aggregateRootType = ast.newSimpleType(
                NamingConventions.aggregateRootTypeName(aggregate).getIdentifier());
        parametrizedType.typeArguments().add(aggregateRootType);

        var attributesType = ast.newSimpleType(NamingConventions.aggregateAttributesQualifiedTypeName(aggregate));
        parametrizedType.typeArguments().add(attributesType);

        return parametrizedType;
    }

    private SimpleType aggregateIdentifierType() {
        return ast.newSimpleType(NamingConventions.aggregateIdentifierTypeName(aggregate).getIdentifier());
    }

    public static class Builder {

        private AggregateFactoryEditor editor = new AggregateFactoryEditor();

        public AggregateFactoryEditor build() {
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

    private AggregateFactoryEditor() {

    }

    private CompilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
