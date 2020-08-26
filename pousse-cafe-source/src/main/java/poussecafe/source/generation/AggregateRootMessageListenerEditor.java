package poussecafe.source.generation;

import java.util.Optional;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.discovery.ProducesEvent;
import poussecafe.source.analysis.Name;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.ComilationUnitEditor;
import poussecafe.source.generation.tools.MethodDeclarationEditor;
import poussecafe.source.generation.tools.NormalAnnotationEditor;
import poussecafe.source.generation.tools.TypeDeclarationEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.DomainEvent;
import poussecafe.source.model.Message;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.Model;
import poussecafe.source.model.ProducedEvent;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class AggregateRootMessageListenerEditor {

    public void edit() {
        compilationUnitEditor.addImportFirst(poussecafe.discovery.MessageListener.class);
        if(!messageListener.producedEvents().isEmpty()) {
            compilationUnitEditor.addImportFirst(ProducesEvent.class);
        }
        for(String processName : messageListener.processNames()) {
            var process = model.process(processName).orElseThrow();
            compilationUnitEditor.addImportFirst(new Name(process.packageName(), process.name()));
        }
        var events = messageListener.producedEvents().stream()
                .map(ProducedEvent::message)
                .map(Message::name)
                .map(model::event)
                .map(Optional::get)
                .collect(toList());
        for(DomainEvent event : events) {
            compilationUnitEditor.addImportFirst(event.name());
        }

        var consumedEvent = model.event(messageListener.consumedMessage().name()).orElseThrow();
        compilationUnitEditor.addImportFirst(consumedEvent.name());

        var typeEditor = compilationUnitEditor.typeDeclaration();

        var listenerMethod = findMethod(typeEditor);
        MethodDeclarationEditor methodEditor;
        if(listenerMethod.isEmpty()) {
            TypeDeclaration attributesType = typeEditor.findTypeDeclarationByName(
                    AggregateCodeGenerationConventions.ATTRIBUTES_CLASS_NAME).orElseThrow();
            methodEditor = typeEditor.insertNewMethodBefore(attributesType);
            methodEditor.addParameter(new Name(messageListener.consumedMessage().name()), "event");
            methodEditor.setBody(ast.ast().newBlock());
        } else {
            methodEditor = typeEditor.editMethod(listenerMethod.get(), false);
        }

        methodEditor.modifiers().setVisibility(Visibility.PUBLIC);
        methodEditor.setName(messageListener.methodName());

        var producesEventEditor = new ProducesEventsEditor.Builder()
                .methodEditor(methodEditor)
                .producedEvents(messageListener.producedEvents())
                .build();
        producesEventEditor.edit();

        NormalAnnotationEditor messageListenerAnnotationEditor;
        if(methodEditor.modifiers().hasAnnotation(poussecafe.discovery.MessageListener.class)) {
            messageListenerAnnotationEditor = methodEditor.modifiers().normalAnnotation(
                    poussecafe.discovery.MessageListener.class).get(0);
        } else {
            messageListenerAnnotationEditor = methodEditor.modifiers().insertNewNormalAnnotationFirst(
                    new Name(poussecafe.discovery.MessageListener.class.getSimpleName()));
        }

        var processesValue = processesValue();
        messageListenerAnnotationEditor.setAttribute("processes", processesValue);

        var runnerType = ast.newTypeLiteral(new Name(messageListener.runnerName().orElseThrow()));
        messageListenerAnnotationEditor.setAttribute("runner", runnerType);

        if(messageListener.consumesFromExternal().isPresent()) {
            messageListenerAnnotationEditor.setAttribute("consumesFromExternal", consumesFromExternalAttributeValue());
        } else {
            messageListenerAnnotationEditor.removeAttribute("consumesFromExternal");
        }

        compilationUnitEditor.flush();
    }

    private Optional<MethodDeclaration> findMethod(TypeDeclarationEditor typeEditor) {
        return typeEditor.findMethods(messageListener.methodName()).stream()
                .filter(this::isMessageListener)
                .filter(this::consumes)
                .findFirst();
    }

    private boolean isMessageListener(MethodDeclaration method) {
        for(Object modifierObject : method.modifiers()) {
            if(modifierObject instanceof Annotation) {
                Annotation annotation = (Annotation) modifierObject;
                if(annotation.getTypeName().isSimpleName()
                        && annotation.getTypeName().getFullyQualifiedName().equals("MessageListener")
                    || annotation.getTypeName().isQualifiedName()
                        && annotation.getTypeName().getFullyQualifiedName().endsWith(".MessageListener")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean consumes(MethodDeclaration method) {
        var eventName = messageListener.consumedMessage().name();
        if(method.parameters().size() == 1) {
            SingleVariableDeclaration parameter = (SingleVariableDeclaration) method.parameters().get(0);
            return parameter.getType().toString().equals(eventName);
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private Expression processesValue() {
        if(messageListener.processNames().isEmpty()) {
            return ast.ast().newNullLiteral();
        } else if(messageListener.processNames().size() == 1) {
            return ast.newTypeLiteral(new Name(messageListener.processNames().get(0)));
        } else {
            var array = ast.ast().newArrayInitializer();
            for(String processName : messageListener.processNames()) {
                array.expressions().add(ast.newTypeLiteral(new Name(processName)));
            }
            return array;
        }
    }

    private Expression consumesFromExternalAttributeValue() {
        Optional<String> consumesFromExternal = messageListener.consumesFromExternal();
        if(consumesFromExternal.isEmpty()) {
            return ast.ast().newNullLiteral();
        } else {
            return ast.newStringLiteral(consumesFromExternal.get());
        }
    }

    private Model model;

    private MessageListener messageListener;

    public static class Builder {

        private AggregateRootMessageListenerEditor editor = new AggregateRootMessageListenerEditor();

        public AggregateRootMessageListenerEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.model);
            requireNonNull(editor.messageListener);

            editor.ast = editor.compilationUnitEditor.ast();

            return editor;
        }

        public Builder compilationUnitEditor(ComilationUnitEditor compilationUnitEditor) {
            editor.compilationUnitEditor = compilationUnitEditor;
            return this;
        }

        public Builder model(Model model) {
            editor.model = model;
            return this;
        }

        public Builder messageListener(MessageListener messageListener) {
            editor.messageListener = messageListener;
            return this;
        }
    }

    private AggregateRootMessageListenerEditor() {

    }

    private ComilationUnitEditor compilationUnitEditor;

    private AstWrapper ast;
}
