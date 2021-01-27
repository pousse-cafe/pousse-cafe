package poussecafe.source.analysis;

import java.util.Optional;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import static java.util.Objects.requireNonNull;

public class TypeDeclarationResolver implements Resolver {

    @Override
    public ResolvedTypeName resolve(Name name) {
        var resolvedInnerClass = resolveInnerClass(tryRemoveDeclaringClassNamePrefix(name));
        if(resolvedInnerClass.isPresent()) {
            return resolvedInnerClass.get();
        } else { // Delegate to parent
            return parent.resolve(name);
        }
    }

    private Name tryRemoveDeclaringClassNamePrefix(Name name) {
        SafeClassName declaringClassName = resolvedTypeDeclaration().unresolvedName();
        if(isPrefixedWithSimpleName(name, declaringClassName)) {
            return new Name(name.toString().substring(declaringClassName.simpleName().length() + 1));
        } else if(isPrefixedWithQualifiedName(name, declaringClassName)) {
            return new Name(name.toString().substring(declaringClassName.qualifiedName().length() + 1));
        } else { // No prefix to remove
            return name;
        }
    }

    private boolean isPrefixedWithSimpleName(Name name, SafeClassName declaringClassName) {
        return !name.toString().equals(declaringClassName.simpleName())
                && name.toString().startsWith(declaringClassName.simpleName());
    }

    private boolean isPrefixedWithQualifiedName(Name name, SafeClassName declaringClassName) {
        return name.toString().startsWith(declaringClassName.qualifiedName());
    }

    private Optional<ResolvedTypeName> resolveInnerClass(Name relativeInnerClassName) {
        var segments = relativeInnerClassName.segments();
        var innerClass = findInnerClassByName(resolvedTypeDeclaration(),
                segments[0]);
        int i = 1;
        while(innerClass.isPresent()
                && i < segments.length) {
            innerClass = findInnerClassByName(innerClass.get(), segments[i]);
            ++i;
        }
        return innerClass.map(ResolvedTypeDeclaration::name);
    }

    private Resolver parent;

    private TypeDeclaration typeDeclaration;

    private Optional<ResolvedTypeDeclaration> parentTypeDeclaration = Optional.empty();

    public ResolvedTypeDeclaration resolvedTypeDeclaration() {
        return new ResolvedTypeDeclaration.Builder()
                .withDeclaration(typeDeclaration)
                .withResolver(this)
                .withDeclaringType(parentTypeDeclaration)
                .withName(name())
                .build();
    }

    private SafeClassName name() {
        return safeClassName;
    }

    private SafeClassName safeClassName;

    private Optional<ResolvedTypeDeclaration> findInnerClassByName(ResolvedTypeDeclaration declaringType,
            String simpleName) {
        for(ResolvedTypeDeclaration innerClass : declaringType.innerTypes()) {
            if(innerClass.unresolvedName().simpleName().equals(simpleName)) {
                return Optional.of(innerClass);
            }
        }
        return Optional.empty();
    }

    public ResolvedMethod resolve(MethodDeclaration method) {
        return new ResolvedMethod.Builder()
                .withResolver(this)
                .withDeclaration(method)
                .withDeclaringType(resolvedTypeDeclaration())
                .build();
    }

    @Override
    public ClassResolver classResolver() {
        return parent.classResolver();
    }

    public static class Builder {

        private TypeDeclarationResolver resolver = new TypeDeclarationResolver();

        public TypeDeclarationResolver build() {
            requireNonNull(resolver.parent);
            requireNonNull(resolver.parentTypeDeclaration);
            requireNonNull(resolver.typeDeclaration);
            requireNonNull(resolver.safeClassName);
            return resolver;
        }

        public Builder parent(Resolver parent) {
            resolver.parent = parent;
            return this;
        }

        public Builder parentTypeDeclaration(Optional<ResolvedTypeDeclaration> parentTypeDeclaration) {
            resolver.parentTypeDeclaration = parentTypeDeclaration;
            return this;
        }

        public Builder typeDeclaration(TypeDeclaration typeDeclaration) {
            resolver.typeDeclaration = typeDeclaration;
            return this;
        }

        public Builder safeClassName(SafeClassName safeClassName) {
            resolver.safeClassName = safeClassName;
            return this;
        }
    }

    private TypeDeclarationResolver() {

    }
}
