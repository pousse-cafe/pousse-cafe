package poussecafe.source.analysis;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class MessageImplementationType {

    public static boolean isMessageImplementation(ResolvedTypeDeclaration type) {
        return type.name().instanceOf(CompilationUnitResolver.MESSAGE_CLASS)
                && isConcreteImplementation(type);
    }

    private static boolean isConcreteImplementation(ResolvedTypeDeclaration type) {
        return !type.typeDeclaration().isInterface()
                && !type.isAbstract()
                && type.asAnnotatedElement().findAnnotation(CompilationUnitResolver.ABSTRACT_MESSAGE_ANNOTATION_CLASS).isEmpty();
    }

    public Optional<ResolvedTypeName> messageName() {
        var annotation = type.asAnnotatedElement().findAnnotation(
                CompilationUnitResolver.MESSAGE_IMPLEMENTATION_ANNOTATION_CLASS);
        return annotation.map(a -> a.attribute("message").orElseThrow().asType());
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

    public MessageImplementationType(ResolvedTypeDeclaration typeName) {
        type = typeName;
    }
}
