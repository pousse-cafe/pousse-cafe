package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import poussecafe.source.analysis.Name;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class AstWrapper {

    public AstWrapper(AST ast) {
        requireNonNull(ast);
        this.ast = ast;
    }

    private AST ast;

    public AST ast() {
        return ast;
    }

    public SimpleType newSimpleType(Class<?> typeClass) {
        return newSimpleType(typeClass.getSimpleName());
    }

    public SimpleType newSimpleType(String name) {
        return ast.newSimpleType(ast.newSimpleName(name));
    }

    public SimpleType newSimpleType(Name name) {
        return ast.newSimpleType(name.toJdomName(ast));
    }

    public TypeDeclarationBuilder newTypeDeclarationBuilder() {
        return new TypeDeclarationBuilder(ast);
    }

    public MethodDeclaration newPublicConstructor(Name typeName) {
        var constructor = ast.newMethodDeclaration();
        constructor.setConstructor(true);
        constructor.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

        String simpleTypeName = typeName.getIdentifier().toString();
        constructor.setName(ast.newSimpleName(simpleTypeName));

        return constructor;
    }

    public SingleVariableDeclaration newSimpleMethodParameter(String simpleTypeName, String parameterName) {
        var constructorParameter = ast.newSingleVariableDeclaration();
        constructorParameter.setType(ast.newSimpleType(ast.newSimpleName(simpleTypeName)));
        constructorParameter.setName(ast.newSimpleName(parameterName));
        return constructorParameter;
    }

    public SimpleName newVariableAccess(String variableName) {
        return ast.newSimpleName(variableName);
    }

    public ParameterizedType newParameterizedType(Class<?> typeClass) {
        return ast.newParameterizedType(newSimpleType(typeClass));
    }

    public ParameterizedType newParameterizedType(Name typeName) {
        return ast.newParameterizedType(newSimpleType(typeName));
    }

    public TypeParameter newExtendingTypeParameter(String name, Name supertype) {
        var parameter = ast.newTypeParameter();
        parameter.setName(ast.newSimpleName(name));
        parameter.typeBounds().add(newSimpleType(supertype));
        return parameter;
    }

    public IExtendedModifier newPublicModifier() {
        return ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD);
    }

    public MarkerAnnotation newOverrideAnnotation() {
        var annotation = ast.newMarkerAnnotation();
        annotation.setTypeName(ast.newSimpleName(Override.class.getSimpleName()));
        return annotation;
    }
}
