package poussecafe.doc.annotations;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
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

    public static Optional<AnnotationMirror> annotation(AnnotatedConstruct annotated, Class<? extends Annotation> annotationClass) {
        return annotated.getAnnotationMirrors()
                .stream()
                .filter(mirror -> mirror.getAnnotationType().asElement().getSimpleName().contentEquals(annotationClass.getSimpleName()))
                .findFirst()
                .map(value -> (AnnotationMirror) value);
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
            ExecutableElement element = entry.getKey();
            if(element.getSimpleName().contentEquals(elementName)) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }

    public static Map<String, AnnotationValue> valuesMap(AnnotationMirror annotationMirror, Set<String> elementNames) {
        Map<String, AnnotationValue> values = new HashMap<>();
        Map<? extends ExecutableElement, ? extends AnnotationValue> annotationValues = annotationMirror.getElementValues();
        for(Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationValues.entrySet()) {
            ExecutableElement element = entry.getKey();
            String elementName = element.getSimpleName().toString();
            if(elementNames.contains(elementName)) {
                values.put(elementName, entry.getValue());
            }
        }
        return values;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(AnnotationValue annotationValue) {
        List<AnnotationValue> values = (List<AnnotationValue>) annotationValue.getValue();
        return values.stream()
                .map(AnnotationValue::getValue)
                .map(value -> (T) value)
                .collect(Collectors.toList());
    }

    public static List<AnnotationMirror> annotations(ExecutableElement methodDoc, Class<? extends Annotation> repeatableAnnotationClass, Class<? extends Annotation> containerAnnotationClass) {
        List<AnnotationMirror> repeatableAnnotations = AnnotationUtils.annotations(methodDoc, repeatableAnnotationClass);
        if(repeatableAnnotations.isEmpty()) {
            List<? extends AnnotationMirror> containerAnnotations = AnnotationUtils.annotations(methodDoc, containerAnnotationClass);
            for(AnnotationMirror mirror : containerAnnotations) {
                Optional<AnnotationValue> value = AnnotationUtils.value(mirror, "value");
                if(value.isPresent()) {
                    repeatableAnnotations.addAll(AnnotationUtils.toList(value.get()));
                }
            }
        }
        return repeatableAnnotations;
    }
}
