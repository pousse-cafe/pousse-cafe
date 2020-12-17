package poussecafe.source.analysis;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class ClassLoaderResolvedClass implements ResolvedClass {

    @Override
    public Name name() {
        return new Name(classObject.getCanonicalName());
    }

    private Class<?> classObject;

    @Override
    public List<ResolvedClass> innerClasses() {
        return Arrays.stream(classObject.getDeclaredClasses())
                .map(this::newResolvedClass)
                .collect(toList());
    }

    private ResolvedClass newResolvedClass(Class<?> classObject) {
        return new ClassLoaderResolvedClass.Builder().classObject(classObject).classResolver(classResolver).build();
    }

    @Override
    public boolean instanceOf(String supertype) throws ClassNotFoundException {
        Optional<ResolvedClass> classLoaderResolvedSupertype = classResolver.loadClass(new Name(supertype));
        if(classLoaderResolvedSupertype.isPresent()) {
            var supertypeClassObject = classLoaderResolvedSupertype
                    .map(resolved -> ((ClassLoaderResolvedClass) resolved).classObject)
                    .orElseThrow();
            return supertypeClassObject.isAssignableFrom(classObject);
        } else {
            throw new ClassNotFoundException(supertype);
        }
    }

    @Override
    public Optional<ResolvedClass> declaringClass() {
        if(classObject.getDeclaringClass() != null) {
            return Optional.of(newResolvedClass(classObject.getDeclaringClass()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ClassResolver resolver() {
        return classResolver;
    }

    private ClassLoaderClassResolver classResolver;

    public static class Builder {

        public ClassLoaderResolvedClass build() {
            requireNonNull(resolver.classObject);
            requireNonNull(resolver.classResolver);
            return resolver;
        }

        private ClassLoaderResolvedClass resolver = new ClassLoaderResolvedClass();

        public Builder classObject(Class<?> classObject) {
            resolver.classObject = classObject;
            return this;
        }

        public Builder classResolver(ClassLoaderClassResolver classResolver) {
            resolver.classResolver = classResolver;
            return this;
        }
    }

    private ClassLoaderResolvedClass() {

    }
}
