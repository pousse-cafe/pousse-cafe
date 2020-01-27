package poussecafe.doc.annotations;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;

public class AnnotationUtils {

    private AnnotationUtils() {

    }

    public static List<AnnotationMirror> annotations(AnnotatedConstruct annotated, Class<? extends Annotation> annotationClass) {
        return annotated.getAnnotationMirrors()
                .stream()
                .filter(mirror -> mirror.getAnnotationType().asElement().getSimpleName().contentEquals(annotationClass.getSimpleName()))
                .collect(Collectors.toList());
    }

    public static List<AnnotationValue> values(List<? extends AnnotationMirror> mirrors, String elementName) {
        List<AnnotationValue> values = new ArrayList<>();
        for(AnnotationMirror annotationMirror : mirrors) {
            Optional<AnnotationValue> value = value(annotationMirror, elementName);
            if(value.isPresent()) {
                values.add(value.get());
            }
        }
        return values;
    }

    public static Optional<AnnotationValue> value(AnnotationMirror annotationMirror, String elementName) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> annotationValues = annotationMirror.getElementValues();
        for(Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationValues.entrySet()) {
            ExecutableElement attribute = entry.getKey();
            if(attribute.getSimpleName().contentEquals(elementName)) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(AnnotationValue annotationValue) {
        List<AnnotationValue> values = (List<AnnotationValue>) annotationValue.getValue();
        return values.stream()
                .map(AnnotationValue::getValue)
                .map(value -> (T) value)
                .collect(Collectors.toList());
    }
}
