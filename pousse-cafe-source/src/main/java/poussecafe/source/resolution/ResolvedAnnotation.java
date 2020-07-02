package poussecafe.source.resolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeLiteral;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

public class ResolvedAnnotation {

    public List<ResolvedTypeName> asTypes(String attributeName) {
        Optional<Expression> expression = attributeValue(attributeName);
        if(expression.isEmpty()) {
            return emptyList();
        } else {
            Expression value = expression.get();
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
    }

    private Optional<ResolvedTypeName> typeName(Expression expression) {
        if(expression instanceof TypeLiteral) {
            TypeLiteral typeLiteral = (TypeLiteral) expression;
            Type type = typeLiteral.getType();
            if(type instanceof SimpleType) {
                SimpleType simpleType = (SimpleType) type;
                return Optional.of(new ResolvedTypeName.Builder()
                        .withImports(imports)
                        .withName(simpleType.getName())
                        .build());
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public Optional<Expression> attributeValue(String attributeName) {
        if(annotation.isNormalAnnotation()) {
            NormalAnnotation normalAnnotation = (NormalAnnotation) annotation;
            for(Object object : normalAnnotation.values()) {
                MemberValuePair pair = (MemberValuePair) object;
                if(pair.getName().getIdentifier().equals(attributeName)) {
                    return Optional.of(pair.getValue());
                }
            }
        }
        return Optional.empty();
    }

    private Imports imports;

    private Annotation annotation;

    public static class Builder {

        private ResolvedAnnotation annotation = new ResolvedAnnotation();

        public ResolvedAnnotation build() {
            requireNonNull(annotation.imports);
            requireNonNull(annotation.annotation);
            return annotation;
        }

        public Builder imports(Imports imports) {
            annotation.imports = imports;
            return this;
        }

        public Builder annotation(Annotation annotation) {
            this.annotation.annotation = annotation;
            return this;
        }
    }

    private ResolvedAnnotation() {

    }
}
