package poussecafe.source.resolution;

import java.util.Optional;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

public class ResolvedMethod {

    public AnnotatedElement<MethodDeclaration> asAnnotatedElement() {
        return new AnnotatedElement.Builder<MethodDeclaration>()
                .withImports(resolver)
                .withElement(declaration)
                .build();
    }

    private Resolver resolver;

    private MethodDeclaration declaration;

    public Optional<ResolvedTypeName> parameterTypeName(int parameter) {
        SingleVariableDeclaration message = (SingleVariableDeclaration) declaration.parameters().get(parameter);
        Type parameterType = message.getType();
        if(parameterType instanceof SimpleType) {
            SimpleType messageType = (SimpleType) message.getType();
            return Optional.of(resolver.resolve(messageType.getName()));
        } else {
            return Optional.empty();
        }
    }

    public String name() {
        return declaration.getName().getIdentifier();
    }

    public static class Builder {

        private ResolvedMethod annotatedElement = new ResolvedMethod();

        public ResolvedMethod build() {
            return annotatedElement;
        }

        public Builder withImports(Resolver resolver) {
            annotatedElement.resolver = resolver;
            return this;
        }

        public Builder withDeclaration(MethodDeclaration declaration) {
            annotatedElement.declaration = declaration;
            return this;
        }
    }

    private ResolvedMethod() {

    }
}
