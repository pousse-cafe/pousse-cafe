package poussecafe.source.generation;

import java.util.Optional;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import poussecafe.discovery.ProducesEvent;
import poussecafe.source.analysis.Name;
import poussecafe.source.generation.tools.AstWrapper;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.generation.tools.MethodDeclarationEditor;
import poussecafe.source.generation.tools.NormalAnnotationEditor;
import poussecafe.source.generation.tools.TypeDeclarationEditor;
import poussecafe.source.generation.tools.Visibility;
import poussecafe.source.model.DomainEvent;
import poussecafe.source.model.Message;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageType;
import poussecafe.source.model.Model;
import poussecafe.source.model.ProducedEvent;

import static java.util.stream.Collectors.toList;

public abstract class AggregateMessageListenerEditor {

    public void edit() {
        compilationUnitEditor.addImport(poussecafe.discovery.MessageListener.class);
        if(!messageListener.producedEvents().isEmpty()) {
            compilationUnitEditor.addImport(ProducesEvent.class);
        }
        for(String processName : messageListener.processNames()) {
            var process = model.process(processName).orElseThrow();
            compilationUnitEditor.addImport(process.name());
        }
        var events = messageListener.producedEvents().stream()
                .map(ProducedEvent::message)
                .map(Message::name)
                .map(model::event)
                .map(Optional::get)
                .collect(toList());
        for(DomainEvent event : events) {
            compilationUnitEditor.addImport(event.name());
        }

        var consumedMessage = messageListener.consumedMessage();
        Name consumedMessageClassName;
        if(consumedMessage.type() == MessageType.COMMAND) {
            consumedMessageClassName = model.command(consumedMessage.name()).orElseThrow().name();
        } else {
            consumedMessageClassName = model.event(consumedMessage.name()).orElseThrow().name();
        }
        compilationUnitEditor.addImport(consumedMessageClassName);

        var typeEditor = compilationUnitEditor.typeDeclaration();

        var listenerMethod = findMethod(typeEditor);
        MethodDeclarationEditor methodEditor;
        if(listenerMethod.isEmpty()) {
            methodEditor = insertNewListener(typeEditor);
            var parameterName = consumedMessage.type() == MessageType.COMMAND ? "command" : "event";
            methodEditor.addParameter(consumedMessageClassName.getIdentifier(), parameterName);
            setBody(methodEditor);
            setReturnType(methodEditor);
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

        setListenerRunner(messageListenerAnnotationEditor);

        if(messageListener.consumesFromExternal().isPresent()) {
            messageListenerAnnotationEditor.setAttribute("consumesFromExternal", consumesFromExternalAttributeValue());
        } else {
            messageListenerAnnotationEditor.removeAttribute("consumesFromExternal");
        }

        compilationUnitEditor.flush();
    }

    protected void setBody(MethodDeclarationEditor methodEditor) {
        if(messageListener.producedEvents().isEmpty()) {
            methodEditor.setEmptyBodyWithComment("TODO: update attributes");
        } else {
            methodEditor.setEmptyBodyWithComment("TODO: update attributes and issue expected events");
        }
    }

    protected void setListenerRunner(NormalAnnotationEditor messageListenerAnnotationEditor) {

    }

    protected void setReturnType(MethodDeclarationEditor methodEditor) {
        methodEditor.setReturnType(null);
    }

    protected abstract MethodDeclarationEditor insertNewListener(TypeDeclarationEditor typeEditor);

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

    protected Model model;

    protected MessageListener messageListener;

    protected CompilationUnitEditor compilationUnitEditor;

    protected AstWrapper ast;
}
