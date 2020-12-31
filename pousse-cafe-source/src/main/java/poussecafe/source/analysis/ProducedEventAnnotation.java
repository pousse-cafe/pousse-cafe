package poussecafe.source.analysis;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class ProducedEventAnnotation {

    public ProducedEventAnnotation(ResolvedAnnotation resolvedAnnotation) {
        event = resolvedAnnotation.attribute("value").orElseThrow().asType();
        required = resolvedAnnotation.attribute("required").map(ResolvedExpression::asBoolean);
        consumedByExternal = resolvedAnnotation.attribute("consumedByExternal")
                .map(ResolvedExpression::asStrings)
                .orElse(emptyList());
    }

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
}
