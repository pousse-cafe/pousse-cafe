package poussecafe.source.analysis;

import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;

import static java.util.Objects.requireNonNull;

public class ResolvedType {

    public boolean isPrimitive() {
        return type.isPrimitiveType();
    }

    private Type type;

    public ResolvedTypeName genericTypeName() {
        if(type instanceof SimpleType) {
            SimpleType simple = (SimpleType) type;
            return resolver.resolve(new Name(simple.getName()));
        } else if(type instanceof ParameterizedType) {
            ParameterizedType parametrized = (ParameterizedType) type;
            SimpleType simple = (SimpleType) parametrized.getType();
            return resolver.resolve(new Name(simple.getName()));
        }
        return null;
    }

    private Resolver resolver;

    public static class Builder {

        private ResolvedType resolved = new ResolvedType();

        public ResolvedType build() {
            requireNonNull(resolved.resolver);
            requireNonNull(resolved.type);
            return resolved;
        }

        public Builder resolver(Resolver resolver) {
            resolved.resolver = resolver;
            return this;
        }

        public Builder type(Type type) {
            resolved.type = type;
            return this;
        }
    }

    private ResolvedType() {

    }
}
