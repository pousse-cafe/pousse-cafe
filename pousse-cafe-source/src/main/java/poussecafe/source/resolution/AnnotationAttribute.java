package poussecafe.source.resolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeLiteral;

import static java.util.Objects.requireNonNull;

public class AnnotationAttribute {

    public List<ResolvedTypeName> asTypes() {
        List<ResolvedTypeName> types = new ArrayList<>();
        if(value instanceof TypeLiteral) {
            types.add(typeName(value).orElseThrow());
        } else if(value instanceof ArrayInitializer) {
            ArrayInitializer initializer = (ArrayInitializer) value;
            for(Object expressionObject : initializer.expressions()) {
                typeName((Expression) expressionObject).ifPresent(types::add);
            }
        }
        return types;
    }

    private Expression value;

    private Optional<ResolvedTypeName> typeName(Expression expression) {
        if(expression instanceof TypeLiteral) {
            TypeLiteral typeLiteral = (TypeLiteral) expression;
            Type type = typeLiteral.getType();
            if(type instanceof SimpleType) {
                SimpleType simpleType = (SimpleType) type;
                return Optional.of(new ResolvedTypeName.Builder()
                        .withImports(resolver)
                        .withName(simpleType.getName())
                        .build());
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    private Resolver resolver;

    public ResolvedTypeName asType() {
        return typeName(value).orElseThrow();
    }

    public boolean asBoolean() {
        if(value instanceof BooleanLiteral) {
            BooleanLiteral literal = (BooleanLiteral) value;
            return literal.booleanValue();
        } else {
            throw new IllegalStateException("Attribute value is not a boolean literal");
        }
    }

    public static class Builder {

        private AnnotationAttribute attribute = new AnnotationAttribute();

        public AnnotationAttribute build() {
            requireNonNull(attribute.value);
            requireNonNull(attribute.resolver);
            return attribute;
        }

        public Builder value(Expression value) {
            attribute.value = value;
            return this;
        }

        public Builder resolver(Resolver resolver) {
            attribute.resolver = resolver;
            return this;
        }
    }

    private AnnotationAttribute() {

    }
}
