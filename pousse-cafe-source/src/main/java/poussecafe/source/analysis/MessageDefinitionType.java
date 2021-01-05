package poussecafe.source.analysis;

import poussecafe.source.model.MessageType;

public class MessageDefinitionType {

    public static boolean isMessageDefinition(ResolvedTypeDeclaration type) {
        return isCommandOrEvent(type)
                && (isAbstractDefinition(type)
                        || isDefinitionAndImplementation(type));
    }

    private static boolean isCommandOrEvent(ResolvedTypeDeclaration type) {
        return type.name().instanceOf(CompilationUnitResolver.COMMAND_INTERFACE)
                || type.name().instanceOf(CompilationUnitResolver.DOMAIN_EVENT_INTERFACE);
    }

    private static boolean isAbstractDefinition(ResolvedTypeDeclaration type) {
        var annotation = type.asAnnotatedElement().findAnnotation(CompilationUnitResolver.ABSTRACT_MESSAGE_ANNOTATION_CLASS);
        return type.typeDeclaration().isInterface() && annotation.isEmpty();
    }

    private static boolean isDefinitionAndImplementation(ResolvedTypeDeclaration type) {
        var annotation = type.asAnnotatedElement().findAnnotation(CompilationUnitResolver.MESSAGE_IMPLEMENTATION_ANNOTATION_CLASS);
        if(annotation.isPresent()) {
            var messageAttribute = annotation.get().attribute("message");
            if(messageAttribute.isPresent()) {
                return messageAttribute.get().asType().resolvedClass().equals(type.name().resolvedClass());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String name() {
        return typeName.name().simpleName();
    }

    private ResolvedTypeDeclaration typeName;

    public MessageType type() {
        if(typeName.name().instanceOf(CompilationUnitResolver.DOMAIN_EVENT_INTERFACE)) {
            return MessageType.DOMAIN_EVENT;
        } else if(typeName.name().instanceOf(CompilationUnitResolver.COMMAND_INTERFACE)) {
            return MessageType.COMMAND;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public MessageDefinitionType(ResolvedTypeDeclaration typeName) {
        this.typeName = typeName;
    }
}
