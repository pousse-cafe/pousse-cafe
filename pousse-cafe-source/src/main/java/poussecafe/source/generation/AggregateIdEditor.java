package poussecafe.source.generation;

import poussecafe.domain.ValueObject;
import poussecafe.source.analysis.Name;
import poussecafe.source.model.Aggregate;
import poussecafe.util.StringId;

import static java.util.Objects.requireNonNull;

public class AggregateIdEditor {

    public void edit() {
        compilationUnitEditor.setPackage(aggregate.packageName());

        compilationUnitEditor.addImportLast(ValueObject.class.getCanonicalName());
        compilationUnitEditor.addImportLast(StringId.class.getCanonicalName());

        var idSupertype = ast.newSimpleType(StringId.class);
        var valueObjectType = ast.newSimpleType(ValueObject.class);

        var typeName = AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate);
        var simpleTypeName = typeName.getIdentifier().toString();
        var typeEditor = compilationUnitEditor.typeDeclaration()
            .setName(simpleTypeName)
            .setSuperclass(idSupertype)
            .addSuperinterface(valueObjectType);

        constructor(typeEditor.constructors(simpleTypeName).get(0));

        typeEditor.modifiers().setVisibility(Visibility.PUBLIC);

        compilationUnitEditor.flush();
    }

    @SuppressWarnings("unchecked")
    private void constructor(MethodDeclarationEditor editor) {
        editor.modifiers().setVisibility(Visibility.PUBLIC);

        editor.clearParameters();
        editor.addParameter(new Name("String"), "value");

        var body = ast.ast().newBlock();
        var superInvocation = ast.ast().newSuperConstructorInvocation();
        superInvocation.arguments().add(ast.newVariableAccess("value"));
        body.statements().add(superInvocation);
        editor.setBody(body);
    }

    private Aggregate aggregate;

    public static class Builder {

        private AggregateIdEditor editor = new AggregateIdEditor();

        public AggregateIdEditor build() {
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

    private AggregateIdEditor() {

    }

    private ComilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
