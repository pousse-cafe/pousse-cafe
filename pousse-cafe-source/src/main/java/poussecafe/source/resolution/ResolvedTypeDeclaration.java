package poussecafe.source.resolution;

import java.util.Optional;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import static java.util.Objects.requireNonNull;

public class ResolvedTypeDeclaration {

    public Optional<ResolvedTypeName> superclass() {
        Type type = declaration.getSuperclassType();
        if(type == null) {
            return Optional.empty();
        } else {
            if(type instanceof ParameterizedType) {
                ParameterizedType parametrizedType = (ParameterizedType) type;
                Type parametrizedTypeType = parametrizedType.getType();
                if(parametrizedTypeType instanceof SimpleType) {
                    ResolvedTypeName name = simpleType((SimpleType) parametrizedTypeType);
                    return Optional.of(name);
                }
            } else if(type instanceof SimpleType) {
                return Optional.of(simpleType((SimpleType) type));
            }
            return Optional.empty();
        }
    }

    private TypeDeclaration declaration;

    private ResolvedTypeName simpleType(SimpleType simpleType) {
        return imports.resolve(simpleType.getName());
    }

    private Imports imports;

    public static class Builder {

        private ResolvedTypeDeclaration type = new ResolvedTypeDeclaration();

        public ResolvedTypeDeclaration build() {
            requireNonNull(type.declaration);
            requireNonNull(type.imports);
            return type;
        }

        public Builder withDeclaration(TypeDeclaration declaration) {
            type.declaration = declaration;
            return this;
        }

        public Builder withImports(Imports imports) {
            type.imports = imports;
            return this;
        }
    }

    private ResolvedTypeDeclaration() {

    }
}
