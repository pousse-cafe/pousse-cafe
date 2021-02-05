package poussecafe.source.analysis;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class SafeClassName implements Serializable {

    public Name rootClassName() {
        return rootClassName;
    }

    private Name rootClassName;

    public List<String> innerClassPath() {
        return Collections.unmodifiableList(innerClassPath);
    }

    private List<String> innerClassPath = new ArrayList<>();

    public boolean isRootClassName() {
        return innerClassPath.isEmpty();
    }

    public Name asName() {
        return new Name(qualifiedName());
    }

    public String qualifiedName() {
        if(isRootClassName()) {
            return rootClassName.qualified();
        } else {
            return rootClassName.qualified() + "." + innerClassPath.stream().collect(joining("."));
        }
    }

    public String simpleName() {
        if(isRootClassName()) {
            return rootClassName.simple();
        } else {
            return innerClassPath.get(innerClassPath.size() - 1);
        }
    }

    public Path toRelativePath() {
        var segments = rootClassName.segments();
        segments[segments.length - 1] = segments[segments.length - 1] + ".java";
        return Path.of("", segments);
    }

    @Override
    public String toString() {
        return qualifiedName();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(rootClassName)
                .append(innerClassPath)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(rootClassName, other.rootClassName)
                .append(innerClassPath, other.innerClassPath)
                .build());
    }

    public static class Builder {

        public SafeClassName build() {
            requireNonNull(safeName.rootClassName);
            requireNonNull(safeName.innerClassPath);
            return safeName;
        }

        private SafeClassName safeName = new SafeClassName();

        public Builder rootClassName(Name rootClassName) {
            safeName.rootClassName = rootClassName;
            return this;
        }

        public Builder innerClassPath(List<String> innerClassPath) {
            innerClassPath.forEach(this::appendPathElement);
            return this;
        }

        public Builder appendPathElement(String innerClassName) {
            safeName.innerClassPath.add(innerClassName);
            return this;
        }
    }

    private SafeClassName() {

    }

    public static SafeClassName ofRootClass(Name name) {
        return new SafeClassName.Builder()
                .rootClassName(name)
                .build();
    }

    public SafeClassName withLastSegment(String lastElement) {
        return new SafeClassName.Builder()
                .rootClassName(rootClassName)
                .innerClassPath(innerClassPath)
                .appendPathElement(lastElement)
                .build();
    }

    public static SafeClassName ofClass(Class<?> classObject) {
        var innerClassPath = new ArrayList<String>();
        var rootClass = classObject;
        while(rootClass.getDeclaringClass() != null) {
            innerClassPath.add(rootClass.getSimpleName());
            rootClass = rootClass.getDeclaringClass();
        }
        return new SafeClassName.Builder()
                .rootClassName(new Name(rootClass.getCanonicalName()))
                .innerClassPath(innerClassPath)
                .build();
    }
}