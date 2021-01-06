package poussecafe.source.analysis;

import java.util.ArrayList;
import java.util.List;
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

    public boolean isParametrized() {
        return type instanceof ParameterizedType;
    }

    public List<ResolvedType> typeParameters() {
        if(isParametrized()) {
            ParameterizedType parametrizedType = (ParameterizedType) type;
            return resolveTypeArguments(parametrizedType.typeArguments());
        } else {
            throw new UnsupportedOperationException("Type is not parametrized");
        }
    }

    @SuppressWarnings("rawtypes")
    private List<ResolvedType> resolveTypeArguments(List typeArguments) {
        var resolvedTypes = new ArrayList<ResolvedType>();
        for(Object typeArgument : typeArguments) {
            resolvedTypes.add(new ResolvedType.Builder()
                    .type((Type) typeArgument)
                    .resolver(resolver)
                    .build());
        }
        return resolvedTypes;
    }

    public boolean isSimpleType() {
        return type instanceof SimpleType;
    }

    public ResolvedTypeName toTypeName() {
        if(isSimpleType()) {
            return resolver.resolve((SimpleType) type);
        } else {
            throw new UnsupportedOperationException();
        }
    }

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
