package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import poussecafe.domain.AggregateFactory;
import poussecafe.source.analysis.Visibility;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.TypeDeclarationEditor;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class AggregateFactoryEditor {

    public void edit() {
        if(typeEditor.isNewType()) {
            compilationUnitEditor.addImport(AggregateFactory.class.getCanonicalName());
            typeEditor.modifiers().setVisibility(Visibility.PUBLIC);

            if(aggregate.innerFactory()) {
                typeEditor.modifiers().setStatic(true);
            } else {
                compilationUnitEditor.setPackage(aggregate.packageName());
                typeEditor.setName(NamingConventions.aggregateFactoryTypeName(aggregate));
            }

            var factorySupertype = factorySupertype();
            typeEditor.setSuperclass(factorySupertype);

            compilationUnitEditor.flush();
        }
    }

    private Aggregate aggregate;

    private ParameterizedType factorySupertype() {
        var parametrizedType = ast.newParameterizedType(AggregateFactory.class);

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

    private AggregateFactoryEditor() {

    }

    private CompilationUnitEditor compilationUnitEditor;

    private TypeDeclarationEditor typeEditor;

    private AstWrapper ast;
}
