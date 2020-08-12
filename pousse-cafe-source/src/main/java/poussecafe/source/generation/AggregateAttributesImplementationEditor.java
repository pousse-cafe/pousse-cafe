package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.ReturnStatement;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.source.analysis.Name;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class AggregateAttributesImplementationEditor {

    public void edit() {
        compilationUnitEditor.setPackage(AggregateCodeGenerationConventions.adaptersPackageName(aggregate));

        compilationUnitEditor.addImportLast(Attribute.class.getCanonicalName());
        compilationUnitEditor.addImportLast(AttributeBuilder.class.getCanonicalName());
        compilationUnitEditor.addImportLast(AggregateCodeGenerationConventions.aggregateRootTypeName(aggregate));
        compilationUnitEditor.addImportLast(AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate));

        var attributesType = ast.newSimpleType(
                AggregateCodeGenerationConventions.aggregateAttributesQualifiedTypeName(aggregate));

        var typeName = AggregateCodeGenerationConventions.aggregateAttributesImplementationTypeName(aggregate);
        var simpleTypeName = typeName.getIdentifier().toString();
        var identifierAttribute = identifierAttribute();
        var identifierField = identifierField();
        var versionField = versionField();
        var aggregateIdentifierTypeDeclaration = ast.newTypeDeclarationBuilder()
            .addModifier(ast.ast().newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD))
            .setName(simpleTypeName)
            .addSuperinterface(attributesType)
            .addMethod(identifierAttribute)
            .addField(identifierField)
            .addField(versionField)
            .build();
        compilationUnitEditor.setDeclaredType(aggregateIdentifierTypeDeclaration);

        compilationUnitEditor.flush();
    }

    private MethodDeclaration identifierAttribute() {
        var method = ast.ast().newMethodDeclaration();
        method.modifiers().add(ast.newOverrideAnnotation());
        method.modifiers().add(ast.newPublicModifier());
        method.setName(ast.ast().newSimpleName(IDENTIFIER_FIELD_NAME));

        var returnType = ast.newParameterizedType(Attribute.class);
        returnType.typeArguments().add(ast.newSimpleType(identifierSimpleName()));
        method.setReturnType2(returnType);

        var body = ast.ast().newBlock();
        var returnAttribute = returnAttribute();
        body.statements().add(returnAttribute);
        method.setBody(body);

        return method;
    }

    private static final String IDENTIFIER_FIELD_NAME = "identifier";

    private Name identifierSimpleName() {
        return AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate).getIdentifier();
    }

    private ReturnStatement returnAttribute() {
        var statement = ast.ast().newReturnStatement();

        var stringId = stringId();
        var read = read(stringId);
        var write = write(read);
        var build = build(write);

        statement.setExpression(build);

        return statement;
    }

    private MethodInvocation stringId() {
        var stringId = ast.ast().newMethodInvocation();
        stringId.setExpression(ast.ast().newSimpleName(AttributeBuilder.class.getSimpleName()));
        stringId.setName(ast.ast().newSimpleName("stringId"));
        var identifierType = ast.ast().newTypeLiteral();
        identifierType.setType(ast.newSimpleType(identifierSimpleName()));
        stringId.arguments().add(identifierType);
        return stringId;
    }

    private MethodInvocation read(MethodInvocation stringId) {
        var read = ast.ast().newMethodInvocation();
        read.setExpression(stringId);
        read.setName(ast.ast().newSimpleName("read"));
        var getter = ast.ast().newLambdaExpression();
        getter.setBody(ast.ast().newSimpleName(IDENTIFIER_FIELD_NAME));
        read.arguments().add(getter);
        return read;
    }

    private MethodInvocation write(MethodInvocation read) {
        var write = ast.ast().newMethodInvocation();
        write.setExpression(read);
        write.setName(ast.ast().newSimpleName("write"));
        var setter = ast.ast().newLambdaExpression();
        var setterParameter = ast.ast().newVariableDeclarationFragment();
        setterParameter.setName(ast.ast().newSimpleName("value"));
        setter.parameters().add(setterParameter);
        var setterAssignment = ast.ast().newAssignment();
        setterAssignment.setLeftHandSide(ast.ast().newSimpleName(IDENTIFIER_FIELD_NAME));
        setterAssignment.setRightHandSide(ast.ast().newSimpleName("value"));
        setter.setBody(setterAssignment);
        write.arguments().add(setter);
        return write;
    }

    private MethodInvocation build(MethodInvocation write) {
        var build = ast.ast().newMethodInvocation();
        build.setExpression(write);
        build.setName(ast.ast().newSimpleName("build"));
        return build;
    }

    private FieldDeclaration identifierField() {
        var declaration = ast.ast().newVariableDeclarationFragment();
        declaration.setName(ast.ast().newSimpleName(IDENTIFIER_FIELD_NAME));

        var fieldDeclaration = ast.ast().newFieldDeclaration(declaration);
        fieldDeclaration.modifiers().add(ast.newPrivateModifier());
        fieldDeclaration.setType(ast.newSimpleType("String"));
        return fieldDeclaration;
    }

    private FieldDeclaration versionField() {
        var declaration = ast.ast().newVariableDeclarationFragment();
        declaration.setName(ast.ast().newSimpleName("version"));

        var fieldDeclaration = ast.ast().newFieldDeclaration(declaration);
        fieldDeclaration.modifiers().add(ast.newSuppressWarningsAnnotation("unused"));
        fieldDeclaration.modifiers().add(ast.newPrivateModifier());
        fieldDeclaration.setType(ast.newSimpleType("Long"));
        return fieldDeclaration;
    }

    private Aggregate aggregate;

    public static class Builder {

        private AggregateAttributesImplementationEditor editor = new AggregateAttributesImplementationEditor();

        public AggregateAttributesImplementationEditor build() {
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

    private AggregateAttributesImplementationEditor() {

    }

    private ComilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
