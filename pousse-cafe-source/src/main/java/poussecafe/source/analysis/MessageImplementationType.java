package poussecafe.source.analysis;

import java.util.List;

import static java.util.Collections.emptyList;

public class MessageImplementationType {

    public static boolean isMessageImplementation(ResolvedTypeDeclaration type) {
        return type.asAnnotatedElement().findAnnotation(
                CompilationUnitResolver.MESSAGE_IMPLEMENTATION_ANNOTATION_CLASS).isPresent();
    }

    public ResolvedTypeName messageName() {
        var annotation = type.asAnnotatedElement().findAnnotation(
                CompilationUnitResolver.MESSAGE_IMPLEMENTATION_ANNOTATION_CLASS).orElseThrow();
        return annotation.attribute("message").orElseThrow().asType();
    }

    private ResolvedTypeDeclaration type;

    public List<String> messagingNames() {
        var annotation = type.asAnnotatedElement().findAnnotation(
                CompilationUnitResolver.MESSAGE_IMPLEMENTATION_ANNOTATION_CLASS);
        if(annotation.isPresent()) {
            var attribute = annotation.get().attribute("messagingNames");
            if(attribute.isPresent()) {
                return attribute.get().asStrings();
            } else {
                return emptyList();
            }
        } else {
            return emptyList();
        }
    }

    public boolean isConcreteImplementation() {
        return !type.typeDeclaration().isInterface()
                && !type.modifiers().isAbstract();
    }

    public boolean implementsMessageInterface() {
        return type.name().instanceOf(CompilationUnitResolver.MESSAGE_CLASS);
    }

    public MessageImplementationType(ResolvedTypeDeclaration typeName) {
        type = typeName;
    }
}
