package poussecafe.source.analysis;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
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

    public List<String> consumedByExternal() {
        return consumedByExternal;
    }

    private List<String> consumedByExternal;

    public static class Builder {

        private ProducedEventAnnotation annotation = new ProducedEventAnnotation();

        public ProducedEventAnnotation build() {
            requireNonNull(annotation.event);
            requireNonNull(annotation.consumedByExternal);
            return annotation;
        }

        public Builder withAnnotation(ResolvedAnnotation resolvedAnnotation) {
            annotation.event = resolvedAnnotation.attribute("value").orElseThrow().asType();
            annotation.required = resolvedAnnotation.attribute("required").map(AnnotationAttribute::asBoolean);
            annotation.consumedByExternal = resolvedAnnotation.attribute("consumedByExternal")
                    .map(AnnotationAttribute::asStrings)
                    .orElse(emptyList());
            return this;
        }
    }

    private ProducedEventAnnotation() {

    }
}
