package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.source.analysis.Name;
import poussecafe.source.analysis.Visibility;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.FieldDeclarationEditor;
import poussecafe.source.generation.tools.MethodDeclarationEditor;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class AggregateAttributesImplementationEditor {

    public void edit() {
        if(compilationUnitEditor.isNew()) {
            compilationUnitEditor.setPackage(NamingConventions.adaptersPackageName(aggregate));

            compilationUnitEditor.addImport(Attribute.class.getCanonicalName());
            compilationUnitEditor.addImport(AttributeBuilder.class.getCanonicalName());
            compilationUnitEditor.addImport(NamingConventions.aggregateRootTypeName(aggregate));
            compilationUnitEditor.addImport(NamingConventions.aggregateIdentifierTypeName(aggregate));

            var typeName = NamingConventions.aggregateAttributesImplementationTypeName(aggregate);
            var typeEditor = compilationUnitEditor.typeDeclaration();
            typeEditor.setName(typeName.getIdentifier().toString());

            var attributesType = ast.newSimpleType(
                    NamingConventions.aggregateAttributesQualifiedTypeName(aggregate));
            typeEditor.addSuperinterface(attributesType);

            var modifiers = typeEditor.modifiers();
            modifiers.setVisibility(Visibility.PUBLIC);

            editIdentifierAttribute(typeEditor.method(IDENTIFIER_FIELD_NAME).get(0));
            editIdentifierField(typeEditor.field(IDENTIFIER_FIELD_NAME).get(0));
            editVersionField(typeEditor.field(VERSION_FIELD_NAME).get(0));

            compilationUnitEditor.flush();
        }
    }

    private void editIdentifierAttribute(MethodDeclarationEditor editor) {
        editor.modifiers().markerAnnotation(Override.class);
        editor.modifiers().setVisibility(Visibility.PUBLIC);

        var returnSimpleType = ast.ast().newSimpleType(ast.ast().newSimpleName(Attribute.class.getSimpleName()));
        var returnType = ast.ast().newParameterizedType(returnSimpleType);
        var identifierSimpleName = ast.ast().newSimpleName(identifierSimpleName().toString());
        returnType.typeArguments().add(ast.ast().newSimpleType(identifierSimpleName));
        editor.setReturnType(returnType);

        var body = ast.ast().newBlock();
        var returnAttribute = returnAttribute();
        body.statements().add(returnAttribute);
        editor.setBody(body);
    }

    public static final String IDENTIFIER_FIELD_NAME = "identifier";

    public static final String VERSION_FIELD_NAME = "version";

    private Name identifierSimpleName() {
        return NamingConventions.aggregateIdentifierTypeName(aggregate).getIdentifier();
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
        setter.setParentheses(false);
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

    private void editIdentifierField(FieldDeclarationEditor editor) {
        editor.modifiers().setVisibility(Visibility.PRIVATE);
        editor.setType(ast.newSimpleType("String"));
    }

    private void editVersionField(FieldDeclarationEditor editor) {
        var suppressWarningsAnnotation = editor.modifiers().singleMemberAnnotation(SuppressWarnings.class);
        var singleString = ast.ast().newStringLiteral();
        singleString.setLiteralValue("unused");
        suppressWarningsAnnotation.get(0).setValue(singleString);

        editor.modifiers().setVisibility(Visibility.PRIVATE);
        editor.setType(ast.newSimpleType("Long"));
    }

    private Aggregate aggregate;

    public static class Builder {

        private AggregateAttributesImplementationEditor editor = new AggregateAttributesImplementationEditor();

        public AggregateAttributesImplementationEditor build() {
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

    private AggregateAttributesImplementationEditor() {

    }

    private CompilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
