package poussecafe.source.analysis;

import java.util.Optional;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import static java.util.Objects.requireNonNull;

public class TypeDeclarationResolver implements Resolver {

    @Override
    public ResolvedTypeName resolve(Name name) {
        var resolvedInnerClass = resolveInnerClass(name, relativeInnerClassName(name));
        if(resolvedInnerClass.isPresent()) {
            return resolvedInnerClass.get();
        } else {
            return parent.resolve(name);
        }
    }

    private Name relativeInnerClassName(Name name) {
        if(!name.toString().equals(containerClass.name().simple())
                && name.toString().startsWith(containerClass.name().simple())) { // Simple name
            return new Name(name.toString().substring(containerClass.name().simple().length() + 1));
        } else if(name.toString().startsWith(containerClass.name().qualified())) { // Canonical name
            return new Name(name.toString().substring(containerClass.name().qualified().length() + 1));
        } else { // No prefix
            return name;
        }
    }

    private Optional<ResolvedTypeName> resolveInnerClass(Name name, Name relativeInnerClassName) {
        if(relativeInnerClassName.isSimpleName()) {
            var innerClass = findInnerClassByName(relativeInnerClassName.toString());
            if(innerClass.isPresent()) {
                return Optional.of(new ResolvedTypeName.Builder()
                        .withName(name)
                        .withResolvedClass(innerClass.get())
                        .withResolver(this)
                        .build());
            } else {
                return Optional.empty();
            }
        } else {
            var innerClassName = relativeInnerClassName.segments()[0];
            var innerClassTypeDeclaration = findInnerTypeDeclarationByName(innerClassName);
            var innerClass = findInnerClassByName(relativeInnerClassName.toString());
            if(innerClassTypeDeclaration.isPresent()) {
                var innerClassResolver = new TypeDeclarationResolver.Builder()
                        .parent(this)
                        .typeDeclaration(innerClassTypeDeclaration.get())
                        .containerClass(innerClass.orElseThrow())
                        .build();
                try {
                    return Optional.of(innerClassResolver.resolve(relativeInnerClassName.withoutFirstSegment()));
                } catch (ResolutionException e) {
                    throw new ResolutionException("Unable to resolve " + name);
                }
            } else {
                return Optional.empty();
            }
        }
    }

    private Optional<TypeDeclaration> findInnerTypeDeclarationByName(String innerClassName) {
        for(TypeDeclaration innerClassTypeDeclaration : typeDeclaration.getTypes()) {
            if(innerClassTypeDeclaration.getName().getFullyQualifiedName().equals(innerClassName)) {
                return Optional.of(innerClassTypeDeclaration);
            }
        }
        return Optional.empty();
    }

    private Resolver parent;

    private TypeDeclaration typeDeclaration;

    private ResolvedClass containerClass;

    public ResolvedClass containerClass() {
        return containerClass;
    }

    private Optional<ResolvedClass> findInnerClassByName(String simpleName) {
        for(ResolvedClass innerClass : containerClass.innerClasses()) {
            if(innerClass.name().simple().equals(simpleName)) {
                return Optional.of(innerClass);
            }
        }
        return Optional.empty();
    }

    public ResolvedMethod resolve(MethodDeclaration method) {
        return new ResolvedMethod.Builder()
                .withResolver(this)
                .withDeclaration(method)
                .build();
    }

    public static class Builder {

        private TypeDeclarationResolver resolver = new TypeDeclarationResolver();

        public TypeDeclarationResolver build() {
            requireNonNull(resolver.parent);
            requireNonNull(resolver.typeDeclaration);
            return resolver;
        }

        public Builder parent(Resolver parent) {
            resolver.parent = parent;
            return this;
        }

        public Builder typeDeclaration(TypeDeclaration typeDeclaration) {
            resolver.typeDeclaration = typeDeclaration;
            return this;
        }

        public Builder containerClass(ResolvedClass typeClass) {
            resolver.containerClass = typeClass;
            return this;
        }
    }

    private TypeDeclarationResolver() {

    }

    @Override
    public ClassResolver classResolver() {
        return parent.classResolver();
    }
}
