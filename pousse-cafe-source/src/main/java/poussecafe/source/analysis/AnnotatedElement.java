package poussecafe.source.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("unchecked")
public class AnnotatedElement<T> {

    public T element() {
        return element;
    }

    private T element;

    public Optional<ResolvedAnnotation> findAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return annotations.stream()
                .filter(annotation -> annotation.isClass(annotationClass))
                .findFirst();
    }

    private List<ResolvedAnnotation> annotations = new ArrayList<>();

    public List<ResolvedAnnotation> findAnnotations(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return annotations.stream()
                .filter(annotation -> annotation.isClass(annotationClass))
                .collect(toList());
    }

    private Resolver resolver;

    public static class Builder<T> {

        private AnnotatedElement<T> annotatedElement = new AnnotatedElement<>();

        public AnnotatedElement<T> build() {
            requireNonNull(annotatedElement.resolver);
            annotatedElement.annotations = annotations.stream().map(annotation -> new ResolvedAnnotation.Builder()
                    .resolver(annotatedElement.resolver)
                    .annotation(annotation)
                    .build())
                    .collect(toList());
            return annotatedElement;
        }

        public Builder<T> withImports(Resolver resolver) {
            annotatedElement.resolver = resolver;
            return this;
        }

        public Builder<T> withElement(T element) {
            annotatedElement.element = element;
            if(element instanceof MethodDeclaration) {
                MethodDeclaration declaration = (MethodDeclaration) element;
                declaration.modifiers().stream()
                        .filter(item -> item instanceof Annotation)
                        .forEach(annotationObject -> annotations.add((Annotation) annotationObject));
            } else {
                throw new IllegalArgumentException("Unsupported annotated element type " + element.getClass().getSimpleName());
            }
            return this;
        }

        private List<Annotation> annotations = new ArrayList<>();
    }

    private AnnotatedElement() {

    }
}
