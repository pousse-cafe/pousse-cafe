package poussecafe.source.analysis;

import java.util.Optional;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ResolvedMethod {

    public AnnotatedElement<MethodDeclaration> asAnnotatedElement() {
        return new AnnotatedElement.Builder<MethodDeclaration>()
                .withResolver(resolver)
                .withElement(declaration)
                .build();
    }

    private Resolver resolver;

    private MethodDeclaration declaration;

    public MethodDeclaration declaration() {
        return declaration;
    }

    public Optional<ResolvedTypeName> parameterTypeName(int parameter) {
        if(declaration.parameters().size() <= parameter) {
            return Optional.empty();
        }

        SingleVariableDeclaration message = (SingleVariableDeclaration) declaration.parameters().get(parameter);
        Type parameterType = message.getType();
        if(parameterType instanceof SimpleType) {
            SimpleType messageType = (SimpleType) message.getType();
            return Optional.of(resolver.resolve(new Name(messageType.getName())));
        } else {
            return Optional.empty();
        }
    }

    public String name() {
        return declaration.getName().getIdentifier();
    }

    public Optional<ResolvedType> returnType() {
        Type returnType = declaration.getReturnType2();
        if(returnType == null
                || isVoid(returnType)) {
            return Optional.empty();
        } else {
            return Optional.of(new ResolvedType.Builder()
                    .resolver(resolver)
                    .type(returnType)
                    .build());
        }
    }

    private boolean isVoid(Type type) {
        if(type instanceof PrimitiveType) {
            var primitiveType = (PrimitiveType) type;
            return primitiveType.getPrimitiveTypeCode() == PrimitiveType.VOID;
        } else {
            return false;
        }
    }

    public Modifiers modifiers() {
        return new Modifiers.Builder()
                .modifiers(declaration.modifiers())
                .resolver(resolver)
                .build();
    }

    public ResolvedTypeDeclaration declaringType() {
        return new ResolvedTypeDeclaration.Builder()
                .withResolver(resolver)
                .withDeclaration((TypeDeclaration) declaration.getParent())
                .build();
    }

    public static class Builder {

        private ResolvedMethod annotatedElement = new ResolvedMethod();

        public ResolvedMethod build() {
            return annotatedElement;
        }

        public Builder withResolver(Resolver resolver) {
            annotatedElement.resolver = resolver;
            return this;
        }

        public Builder withDeclaration(MethodDeclaration declaration) {
            annotatedElement.declaration = declaration;
            return this;
        }
    }

    private ResolvedMethod() {

    }
}
