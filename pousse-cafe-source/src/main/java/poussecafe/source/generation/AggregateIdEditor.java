package poussecafe.source.generation;

import poussecafe.domain.ValueObject;
import poussecafe.source.analysis.Name;
import poussecafe.source.analysis.Visibility;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.MethodDeclarationEditor;
import poussecafe.source.model.Aggregate;
import poussecafe.util.StringId;

import static java.util.Objects.requireNonNull;

public class AggregateIdEditor {

    public void edit() {
        if(compilationUnitEditor.isNew()) {
            compilationUnitEditor.setPackage(aggregate.packageName());

            compilationUnitEditor.addImport(ValueObject.class.getCanonicalName());
            compilationUnitEditor.addImport(StringId.class.getCanonicalName());

            var typeEditor = compilationUnitEditor.typeDeclaration();
            typeEditor.modifiers().setVisibility(Visibility.PUBLIC);

            var typeName = NamingConventions.aggregateIdentifierTypeName(aggregate);
            var simpleTypeName = typeName.getIdentifier().toString();
            typeEditor.setName(simpleTypeName);

            var idSupertype = ast.newSimpleType(StringId.class);
            typeEditor.setSuperclass(idSupertype);

            var valueObjectType = ast.newSimpleType(ValueObject.class);
            typeEditor.addSuperinterface(valueObjectType);

            constructor(typeEditor.constructors(simpleTypeName).get(0));

            compilationUnitEditor.flush();
        }
    }

    @SuppressWarnings("unchecked")
    private void constructor(MethodDeclarationEditor editor) {
        if(editor.isNewNode()) {
            editor.modifiers().setVisibility(Visibility.PUBLIC);

            editor.clearParameters();
            editor.addParameter(new Name("String"), "value");

            var body = ast.ast().newBlock();
            var superInvocation = ast.ast().newSuperConstructorInvocation();
            superInvocation.arguments().add(ast.newVariableAccess("value"));
            body.statements().add(superInvocation);
            editor.setBody(body);
        }
    }

    private Aggregate aggregate;

    public static class Builder {

        private AggregateIdEditor editor = new AggregateIdEditor();

        public AggregateIdEditor build() {
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

    private AggregateIdEditor() {

    }

    private CompilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
