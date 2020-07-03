package poussecafe.source.model;

import java.util.List;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.source.resolution.AnnotatedElement;
import poussecafe.source.resolution.AnnotationAttribute;
import poussecafe.source.resolution.ResolvedAnnotation;
import poussecafe.source.resolution.ResolvedTypeName;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class MessageListenerAnnotations {

    public static boolean isMessageListener(AnnotatedElement<MethodDeclaration> annotatedElement) {
        return annotatedElement.findAnnotation(MessageListener.class).isPresent();
    }

    public MessageListenerAnnotations(AnnotatedElement<MethodDeclaration> annotatedMethod) {
        setProcessNames(annotatedMethod.findAnnotation(MessageListener.class).orElseThrow());
        setProducedEvents(annotatedMethod.findAnnotations(ProducesEvent.class));
    }

    private void setProcessNames(ResolvedAnnotation messageListenerAnnotation) {
        processes = messageListenerAnnotation.attributeValue("processes")
                .map(AnnotationAttribute::asTypes)
                .orElse(emptyList());
    }

    private List<ResolvedTypeName> processes;

    public List<ResolvedTypeName> processes() {
        return processes;
    }

    public void setProducedEvents(List<ResolvedAnnotation> annotations) {
        producedEvents = annotations.stream()
                .map(annotation -> new ProducedEventAnnotation.Builder()
                        .withAnnotation(annotation)
                        .build())
                .collect(toList());
    }

    private List<ProducedEventAnnotation> producedEvents;

    public List<ProducedEventAnnotation> producedEvents() {
        return producedEvents;
    }
}
