package poussecafe.source.generation.tools;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import poussecafe.source.analysis.ClassName;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class TypeDeclarationBuilder {

    public TypeDeclarationBuilder setName(String name) {
        typeDeclaration().setName(ast.newSimpleName(name));
        return this;
    }

    public TypeDeclarationBuilder setName(ClassName name) {
        setName(name.getIdentifier().toString());
        return this;
    }

    private TypeDeclaration typeDeclaration() {
        if(typeDeclaration == null) {
            typeDeclaration = ast.newTypeDeclaration();
        }
        return typeDeclaration;
    }

    private TypeDeclaration typeDeclaration;

    public TypeDeclaration build() {
        return typeDeclaration;
    }

    public TypeDeclarationBuilder setSuperclass(Type superclassType) {
        typeDeclaration().setSuperclassType(superclassType);
        return this;
    }

    public TypeDeclarationBuilder addModifier(IExtendedModifier modifier) {
        typeDeclaration().modifiers().add(modifier);
        return this;
    }

    public TypeDeclarationBuilder addDeclaredType(TypeDeclaration type) {
        typeDeclaration().bodyDeclarations().add(type);
        return this;
    }

    public TypeDeclarationBuilder setInterface(boolean isInterface) {
        typeDeclaration().setInterface(isInterface);
        return this;
    }

    public TypeDeclarationBuilder addSuperinterface(Type superinterfaceType) {
        typeDeclaration().superInterfaceTypes().add(superinterfaceType);
        return this;
    }

    public TypeDeclarationBuilder addMethod(MethodDeclaration method) {
        typeDeclaration().bodyDeclarations().add(method);
        return this;
    }

    public TypeDeclarationBuilder addTypeParameter(TypeParameter parameter) {
        typeDeclaration().typeParameters().add(parameter);
        return this;
    }

    public TypeDeclarationBuilder(AST ast) {
        requireNonNull(ast);
        this.ast = ast;
    }

    private AST ast;

    public TypeDeclarationBuilder addField(FieldDeclaration identifierField) {
        typeDeclaration().bodyDeclarations().add(identifierField);
        return this;
    }
}
