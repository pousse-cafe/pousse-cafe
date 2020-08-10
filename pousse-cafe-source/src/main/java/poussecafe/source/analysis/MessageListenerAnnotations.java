package poussecafe.source.analysis;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class MessageListenerAnnotations {

    public static boolean isMessageListener(AnnotatedElement<MethodDeclaration> annotatedElement) {
        return annotatedElement.findAnnotation(CompilationUnitResolver.MESSAGE_LISTENER_ANNOTATION_CLASS).isPresent();
    }

    public MessageListenerAnnotations(AnnotatedElement<MethodDeclaration> annotatedMethod) {
        ResolvedAnnotation messageListenerAnnotation = annotatedMethod.findAnnotation(CompilationUnitResolver.MESSAGE_LISTENER_ANNOTATION_CLASS).orElseThrow();

        setProcessNames(messageListenerAnnotation);
        setRunner(messageListenerAnnotation);
        setConsumesFromExternal(messageListenerAnnotation);

        setProducedEvents(annotatedMethod.findAnnotations(CompilationUnitResolver.PRODUCES_EVENT_ANNOTATION_CLASS));
    }

    private void setProcessNames(ResolvedAnnotation messageListenerAnnotation) {
        processes = messageListenerAnnotation.attribute("processes")
                .map(AnnotationAttribute::asTypes)
                .orElse(emptyList());
    }

    private List<ResolvedTypeName> processes;

    public List<ResolvedTypeName> processes() {
        return processes;
    }

    public void setProducedEvents(List<ResolvedAnnotation> annotations) {
        producedEvents = annotations.stream().map(ProducedEventAnnotation::new).collect(toList());
    }

    private List<ProducedEventAnnotation> producedEvents;

    public List<ProducedEventAnnotation> producedEvents() {
        return producedEvents;
    }

    private void setRunner(ResolvedAnnotation messageListenerAnnotation) {
        runner = messageListenerAnnotation.attribute("runner").map(AnnotationAttribute::asType);
    }

    private Optional<ResolvedTypeName> runner;

    public Optional<ResolvedTypeName> runner() {
        return runner;
    }

    private void setConsumesFromExternal(ResolvedAnnotation messageListenerAnnotation) {
        consumesFromExternal = messageListenerAnnotation.attribute("consumesFromExternal")
                .map(AnnotationAttribute::asString);
    }

    private Optional<String> consumesFromExternal;

    public Optional<String> consumesFromExternal() {
        return consumesFromExternal;
    }
}
