package poussecafe.source.analysis;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import poussecafe.source.PathSource;
import poussecafe.source.Source;
import poussecafe.util.Equality;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class ClassLoaderResolvedClass implements ResolvedClass {

    @Override
    public ClassName name() {
        return new ClassName(classObject.getCanonicalName());
    }

    private Class<?> classObject;

    public Class<?> classObject() {
        return classObject;
    }

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
        Optional<ResolvedClass> classLoaderResolvedSupertype = classResolver.loadClass(new ClassName(supertype));
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

    @Override
    public Optional<Object> staticFieldValue(String fieldName) {
        try {
            var constantField = classObject.getDeclaredField(fieldName);
            if(Modifier.isStatic(constantField.getModifiers())) {
                return Optional.ofNullable(constantField.get(null));
            } else {
                throw new IllegalArgumentException("Field " + fieldName + " is not static");
            }
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Source source() {
        return new PathSource(SafeClassName.ofClass(classObject).toRelativePath());
    }

    @Override
    public boolean isInterface() {
        return classObject.isInterface();
    }

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

    @Override
    public int hashCode() {
        return classObject.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return Equality.referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(classObject, other.classObject)
                .build());
    }
}
