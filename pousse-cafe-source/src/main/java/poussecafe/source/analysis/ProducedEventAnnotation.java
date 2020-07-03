package poussecafe.source.analysis;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class ProducedEventAnnotation {

    public ResolvedTypeName event() {
        return event;
    }

    private ResolvedTypeName event;

    public Optional<Boolean> required() {
        return required;
    }

    private Optional<Boolean> required;

    public static class Builder {

        private ProducedEventAnnotation annotation = new ProducedEventAnnotation();

        public ProducedEventAnnotation build() {
            requireNonNull(annotation.event);
            return annotation;
        }

        public Builder withAnnotation(ResolvedAnnotation resolvedAnnotation) {
            annotation.event = resolvedAnnotation.attribute("value").orElseThrow().asType();
            annotation.required = resolvedAnnotation.attribute("required").map(AnnotationAttribute::asBoolean);
            return this;
        }
    }

    private ProducedEventAnnotation() {

    }
}
