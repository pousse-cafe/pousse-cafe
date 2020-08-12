package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
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
        var constuctor = constructor();

        var typeName = AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate);
        var simpleTypeName = typeName.getIdentifier().toString();
        var aggregateIdentifierTypeDeclaration = ast.newTypeDeclarationBuilder()
            .addModifier(ast.ast().newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD))
            .setName(simpleTypeName)
            .setSuperclass(idSupertype)
            .addSuperinterface(valueObjectType)
            .addMethod(constuctor)
            .build();
        compilationUnitEditor.setDeclaredType(aggregateIdentifierTypeDeclaration);

        compilationUnitEditor.flush();
    }

    @SuppressWarnings("unchecked")
    private MethodDeclaration constructor() {
        Name typeName = AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate);
        var constuctor = ast.newPublicConstructor(typeName);

        var constructorParameter = ast.newSimpleMethodParameter("String", "value");
        constuctor.parameters().add(constructorParameter);

        var body = ast.ast().newBlock();
        var superInvocation = ast.ast().newSuperConstructorInvocation();
        superInvocation.arguments().add(ast.newVariableAccess("value"));
        body.statements().add(superInvocation);
        constuctor.setBody(body);

        return constuctor;
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
