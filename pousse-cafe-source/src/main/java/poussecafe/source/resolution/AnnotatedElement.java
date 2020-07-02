package poussecafe.source.resolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;

@SuppressWarnings("unchecked")
public class AnnotatedElement<T> {

    public T element() {
        return element;
    }

    private T element;

    public Optional<ResolvedAnnotation> findAnnotation(Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return annotations.stream()
                .filter(annotation -> imports.resolve(annotation.getTypeName()).isClass(annotationClass))
                .findFirst().map(annotation -> new ResolvedAnnotation.Builder()
                        .imports(imports)
                        .annotation(annotation)
                        .build());
    }

    private List<Annotation> annotations = new ArrayList<>();

    private Imports imports;

    public static class Builder<T> {

        private AnnotatedElement<T> annotatedElement = new AnnotatedElement<>();

        public AnnotatedElement<T> build() {
            return annotatedElement;
        }

        public Builder<T> withImports(Imports imports) {
            annotatedElement.imports = imports;
            return this;
        }

        public Builder<T> withElement(T element) {
            annotatedElement.element = element;
            if(element instanceof MethodDeclaration) {
                MethodDeclaration declaration = (MethodDeclaration) element;
                declaration.modifiers().stream()
                        .filter(item -> item instanceof Annotation)
                        .forEach(annotationObject -> annotatedElement.annotations.add((Annotation) annotationObject));
            } else {
                throw new IllegalArgumentException("Unsupported annotated element type " + element.getClass().getSimpleName());
            }
            return this;
        }
    }

    private AnnotatedElement() {

    }
}
