package poussecafe.source.analysis;

import java.util.Optional;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import static java.util.Objects.requireNonNull;

public class ResolvedTypeDeclaration {

    public Optional<ResolvedTypeName> superclass() {
        Type type = declaration.getSuperclassType();
        return typeName(type);
    }

    private Optional<ResolvedTypeName> typeName(Type type) {
        if(type == null) {
            return Optional.empty();
        } else {
            if(type instanceof ParameterizedType) {
                ParameterizedType parametrizedType = (ParameterizedType) type;
                Type parametrizedTypeType = parametrizedType.getType();
                if(parametrizedTypeType instanceof SimpleType) {
                    ResolvedTypeName name = simpleType((SimpleType) parametrizedTypeType);
                    return Optional.of(name);
                }
            } else if(type instanceof SimpleType) {
                return Optional.of(simpleType((SimpleType) type));
            }
            return Optional.empty();
        }
    }

    private TypeDeclaration declaration;

    private ResolvedTypeName simpleType(SimpleType simpleType) {
        return resolver.resolve(new Name(simpleType.getName()));
    }

    private Resolver resolver;

    public ResolvedTypeName name() {
        return resolver.resolve(new Name(declaration.getName()));
    }

    public boolean implementsInterface(String interfaceClass) {
        for(Object object : declaration.superInterfaceTypes()) {
            Optional<ResolvedTypeName> typeName = typeName((Type) object);
            if(typeName.isPresent() && typeName.get().isClass(interfaceClass)) {
                return true;
            }
        }
        return false;
    }

    public ResolvedTypeName typeParameter(int parameter) {
        Type type = declaration.getSuperclassType();
        if(type instanceof ParameterizedType) {
            ParameterizedType parametrizedType = (ParameterizedType) type;
            Type typeParameter = (Type) parametrizedType.typeArguments().get(parameter);
            if(typeParameter instanceof SimpleType) {
                return simpleType((SimpleType) typeParameter);
            } else {
                throw new UnsupportedOperationException("Unsupported parameter type " + typeParameter.getClass());
            }
        } else {
            throw new UnsupportedOperationException("Type is not parametrized");
        }
    }

    public AnnotatedElement<TypeDeclaration> asAnnotatedElement() {
        return new AnnotatedElement.Builder<TypeDeclaration>()
                .withElement(declaration)
                .withResolver(resolver)
                .build();
    }

    public TypeDeclaration typeDeclaration() {
        return declaration;
    }

    public Modifiers modifiers() {
        return new Modifiers.Builder()
                .modifiers(declaration.modifiers())
                .resolver(resolver)
                .build();
    }

    public boolean isConcrete() {
        return !declaration.isInterface()
                && !modifiers().isAbstract();
    }

    public boolean isInnerClass() {
        return declaration.getParent() instanceof TypeDeclaration;
    }

    public ResolvedTypeDeclaration declaringType() {
        return new Builder()
                .withDeclaration((TypeDeclaration) declaration.getParent())
                .withResolver(resolver)
                .build();
    }

    public Name className() {
        return name().resolvedClass().name();
    }

    public static class Builder {

        private ResolvedTypeDeclaration type = new ResolvedTypeDeclaration();

        public ResolvedTypeDeclaration build() {
            requireNonNull(type.declaration);
            requireNonNull(type.resolver);
            return type;
        }

        public Builder withDeclaration(TypeDeclaration declaration) {
            type.declaration = declaration;
            return this;
        }

        public Builder withResolver(Resolver resolver) {
            type.resolver = resolver;
            return this;
        }
    }

    private ResolvedTypeDeclaration() {

    }
}
