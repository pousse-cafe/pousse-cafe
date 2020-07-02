package poussecafe.source.model;

import java.util.List;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import poussecafe.discovery.DefaultProcess;
import poussecafe.discovery.MessageListener;
import poussecafe.source.resolution.AnnotatedElement;
import poussecafe.source.resolution.ResolvedAnnotation;
import poussecafe.source.resolution.ResolvedTypeName;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class MessageListenerAnnotation {

    public MessageListenerAnnotation(AnnotatedElement<MethodDeclaration> annotatedMethod) {
        resolvedAnnotation = annotatedMethod.findAnnotation(MessageListener.class).orElseThrow();
    }

    private ResolvedAnnotation resolvedAnnotation;

    public List<String> processNames() {
        List<ResolvedTypeName> processes = resolvedAnnotation.asTypes("processes");
        if(processes.isEmpty()) {
            return singletonList(DefaultProcess.class.getSimpleName());
        } else {
            return processes.stream()
                    .map(ResolvedTypeName::simpleName)
                    .collect(toList());
        }
    }
}
