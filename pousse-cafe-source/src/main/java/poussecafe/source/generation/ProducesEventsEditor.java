package poussecafe.source.generation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeLiteral;
import poussecafe.discovery.ProducesEvent;
import poussecafe.source.analysis.Name;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.MethodDeclarationEditor;
import poussecafe.source.generation.tools.ModifiersEditor;
import poussecafe.source.generation.tools.NormalAnnotationEditor;
import poussecafe.source.generation.tools.SingleMemberAnnotationEditor;
import poussecafe.source.model.ProducedEvent;

import static java.util.Objects.requireNonNull;

public class ProducesEventsEditor {

    public void edit() {
        for(ProducedEvent producedEvent : producedEvents) {
            editProducesEventAnnotation(methodEditor, producedEvent);
        }
    }

    private List<ProducedEvent> producedEvents = new ArrayList<>();

    private MethodDeclarationEditor methodEditor;

    private void editProducesEventAnnotation(MethodDeclarationEditor editor, ProducedEvent producedEvent) {
        Optional<Annotation> producedEventAnnotation = findAnnotationMatching(editor, producedEvent.message().name());
        var modifiers = editor.modifiers();
        if(producedEventAnnotation.isEmpty()) {
            addProducesEventAnnotation(modifiers, producedEvent);
        } else if(!annotationMatches(producedEventAnnotation.get(), producedEvent)) {
            modifiers.removeAnnotation(producedEventAnnotation.get());
            addProducesEventAnnotation(modifiers, producedEvent);
        } else {
            editProducesEventAnnotation(modifiers, producedEventAnnotation.get(), producedEvent);
        }
    }

    private Optional<Annotation> findAnnotationMatching(MethodDeclarationEditor methodEditor,
            String eventName) {
        var producesEventAnnotations = methodEditor.modifiers().findAnnotations(ProducesEvent.class);
        for(Annotation annotation : producesEventAnnotations) {
            if(annotationMatches(eventName, annotation)) {
                return Optional.of(annotation);
            }
        }
        return Optional.empty();
    }

    private boolean annotationMatches(String eventName, Annotation annotation) {
        return (annotation.isSingleMemberAnnotation()
                    && singleMemberAnnotationMatches((SingleMemberAnnotation) annotation, eventName))
                || (annotation.isNormalAnnotation()
                        && normalAnnotationMatches((NormalAnnotation) annotation, eventName));
    }

    private boolean singleMemberAnnotationMatches(SingleMemberAnnotation annotation, String eventName) {
        var value = annotation.getValue();
        return annotationValueIsSimpleType(eventName, value);
    }

    private boolean annotationValueIsSimpleType(String eventName, Expression value) {
        if(value instanceof TypeLiteral) {
            TypeLiteral literal = (TypeLiteral) value;
            if(literal.getType() instanceof SimpleType) {
                var simpleType = (SimpleType) literal.getType();
                return simpleType.getName().getFullyQualifiedName().equals(eventName);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean normalAnnotationMatches(NormalAnnotation annotation, String eventName) {
        var value = findNormalAnnotationValue(annotation, "value");
        return value.isPresent()
                && annotationValueIsSimpleType(eventName, value.get());
    }

    private Optional<Expression> findNormalAnnotationValue(NormalAnnotation annotation, String attributeName) {
        for(Object valuePairObject : annotation.values()) {
            MemberValuePair valuePair = (MemberValuePair) valuePairObject;
            if(valuePair.getName().getIdentifier().equals(attributeName)) {
                return Optional.of(valuePair.getValue());
            }
        }
        return Optional.empty();
    }

    private void addProducesEventAnnotation(ModifiersEditor modifiers, ProducedEvent producedEvent) {
        if(producedEvent.required()
                && producedEvent.consumedByExternal().isEmpty()) {
            addProducesEventSingleMemberAnnotation(modifiers, producedEvent);
        } else {
            addProducesEventNormalAnnotation(modifiers, producedEvent);
        }
    }

    private void addProducesEventSingleMemberAnnotation(ModifiersEditor modifiers, ProducedEvent producedEvent) {
        var annotationEditor = modifiers.singleMemberAnnotation(ProducesEvent.class).get(0);
        setSingleValue(producedEvent, annotationEditor);
    }

    private void setSingleValue(ProducedEvent producedEvent, SingleMemberAnnotationEditor annotationEditor) {
        annotationEditor.setValue(ast.newTypeLiteral(new Name(producedEvent.message().name())));
    }

    private AstWrapper ast;

    private void addProducesEventNormalAnnotation(ModifiersEditor modifiers, ProducedEvent producedEvent) {
        var annotationEditor = modifiers.normalAnnotation(ProducesEvent.class).get(0);
        setAttributes(producedEvent, annotationEditor);
    }

    private void setAttributes(ProducedEvent producedEvent, NormalAnnotationEditor annotationEditor) {
        annotationEditor.setAttribute("value", ast.newTypeLiteral(new Name(producedEvent.message().name())));
        if(!producedEvent.required()) {
            annotationEditor.setAttribute("required", ast.ast().newBooleanLiteral(false));
        }
        if(producedEvent.consumedByExternal().size() == 1) {
            annotationEditor.setAttribute("consumedByExternal",
                    ast.newStringLiteral(producedEvent.consumedByExternal().get(0)));
        } else if(producedEvent.consumedByExternal().size() > 1) {
            annotationEditor.setAttribute("consumedByExternal",
                    ast.newStringArrayInitializer(producedEvent.consumedByExternal()));
        }
    }

    private boolean annotationMatches(Annotation annotation, ProducedEvent producedEvent) {
        if(producedEvent.required()
                && producedEvent.consumedByExternal().isEmpty()) {
            return annotation.isSingleMemberAnnotation();
        } else {
            return annotation.isNormalAnnotation();
        }
    }

    private void editProducesEventAnnotation(ModifiersEditor modifiers, Annotation annotation, ProducedEvent producedEvent) {
        if(producedEvent.required()
                && producedEvent.consumedByExternal().isEmpty()) {
            editSingleMemberAnnotation(modifiers, annotation, producedEvent);
        } else {
            editNormalAnnotation(modifiers, annotation, producedEvent);
        }
    }

    private void editSingleMemberAnnotation(ModifiersEditor modifiers, Annotation annotation, ProducedEvent producedEvent) {
        var editor = modifiers.singleMemberAnnotationEditor(annotation);
        setSingleValue(producedEvent, editor);
    }

    private void editNormalAnnotation(ModifiersEditor modifiers, Annotation annotation, ProducedEvent producedEvent) {
        var editor = modifiers.normalAnnotationEditor(annotation);
        setAttributes(producedEvent, editor);
    }

    public static class Builder {

        private ProducesEventsEditor editor = new ProducesEventsEditor();

        public ProducesEventsEditor build() {
            requireNonNull(editor.methodEditor);

            editor.ast = editor.methodEditor.ast();

            return editor;
        }

        public Builder methodEditor(MethodDeclarationEditor methodEditor) {
            editor.methodEditor = methodEditor;
            return this;
        }

        public Builder producedEvents(Collection<ProducedEvent> producedEvents) {
            editor.producedEvents.addAll(producedEvents);
            return this;
        }
    }

    private ProducesEventsEditor() {

    }
}
