package poussecafe.source.model;

import java.util.Optional;
import poussecafe.source.resolution.AnnotationAttribute;
import poussecafe.source.resolution.ResolvedAnnotation;
import poussecafe.source.resolution.ResolvedTypeName;

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
            annotation.event = resolvedAnnotation.attributeValue("value").orElseThrow().asType();
            annotation.required = resolvedAnnotation.attributeValue("required").map(AnnotationAttribute::asBoolean);
            return this;
        }
    }

    private ProducedEventAnnotation() {

    }
}
