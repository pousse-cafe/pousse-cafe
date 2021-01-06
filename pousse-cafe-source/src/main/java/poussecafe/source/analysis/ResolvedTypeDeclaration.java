package poussecafe.source.analysis;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

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
                    ResolvedTypeName name = resolver.resolve((SimpleType) parametrizedTypeType);
                    return Optional.of(name);
                }
            } else if(type instanceof SimpleType) {
                return Optional.of(resolver.resolve((SimpleType) type));
            }
            return Optional.empty();
        }
    }

    private TypeDeclaration declaration;

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

    public Optional<ResolvedType> superclassType() {
        return Optional.ofNullable(declaration.getSuperclassType()).map(this::resolveType);
    }

    private ResolvedType resolveType(Type type) {
        return new ResolvedType.Builder()
                .type(type)
                .resolver(resolver)
                .build();
    }

    @SuppressWarnings("unchecked")
    public List<ResolvedType> superInterfaceTypes() {
        return ((List<Type>) declaration.superInterfaceTypes()).stream()
                .map(this::resolveType)
                .collect(toList());
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
