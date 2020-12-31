package poussecafe.source.analysis;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class MessageListenerMethod {

    public static boolean isMessageListener(ResolvedMethod annotatedElement) {
        return annotatedElement.asAnnotatedElement().findAnnotation(CompilationUnitResolver.MESSAGE_LISTENER_ANNOTATION_CLASS).isPresent();
    }

    public static boolean isMessageListenerMethodContainer(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        return AggregateRootClass.isAggregateRoot(resolvedTypeDeclaration)
                || FactoryClass.isFactory(resolvedTypeDeclaration)
                || RepositoryClass.isRepository(resolvedTypeDeclaration)
                || AggregateContainerClass.isAggregateContainerClass(resolvedTypeDeclaration);
    }

    public MessageListenerMethod(ResolvedMethod method) {
        if(!isMessageListener(method)) {
            throw new IllegalArgumentException();
        }
        this.method = method;
    }

    private ResolvedMethod method;

    public String name() {
        return method.name();
    }

    public Optional<ResolvedTypeName> consumedMessage() {
        return method.parameterTypeName(0);
    }

    public List<ResolvedTypeName> processes() {
        return messageListenerAnnotation().attribute("processes")
                .map(ResolvedExpression::asTypes)
                .orElse(emptyList());
    }

    private ResolvedAnnotation messageListenerAnnotation() {
        return method.asAnnotatedElement().findAnnotation(CompilationUnitResolver.MESSAGE_LISTENER_ANNOTATION_CLASS).orElseThrow();
    }

    public List<ProducedEventAnnotation> producedEvents() {
        var annotations = method.asAnnotatedElement().findAnnotations(CompilationUnitResolver.PRODUCES_EVENT_ANNOTATION_CLASS);
        return annotations.stream().map(ProducedEventAnnotation::new).collect(toList());
    }

    public Optional<ResolvedTypeName> runner() {
        return messageListenerAnnotation().attribute("runner").map(ResolvedExpression::asType);
    }

    public Optional<String> consumesFromExternal() {
        return messageListenerAnnotation().attribute("consumesFromExternal").map(ResolvedExpression::asString);
    }

    public Optional<ResolvedType> returnType() {
        return method.returnType();
    }
}
