package poussecafe.source.analysis;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class EntityImplementationType {

    public static boolean isEntityImplementation(ResolvedTypeDeclaration type) {
        return type.name().instanceOf(CompilationUnitResolver.ENTITY_ATTRIBUTES_INTERFACE)
                && isConcreteImplementation(type);
    }

    private static boolean isConcreteImplementation(ResolvedTypeDeclaration type) {
        return !type.isAbstract();
    }

    public Optional<ResolvedTypeName> entity() {
        var annotation = type.asAnnotatedElement().findAnnotation(
                CompilationUnitResolver.DATA_IMPLEMENTATION_ANNOTATION_CLASS);
        return annotation.map(a -> a.attribute("entity").orElseThrow().asType());
    }

    private ResolvedTypeDeclaration type;

    public List<String> storageNames() {
        var annotation = type.asAnnotatedElement().findAnnotation(
                CompilationUnitResolver.MESSAGE_IMPLEMENTATION_ANNOTATION_CLASS);
        if(annotation.isPresent()) {
            var attribute = annotation.get().attribute("storageNames");
            if(attribute.isPresent()) {
                return attribute.get().asStrings();
            } else {
                return emptyList();
            }
        } else {
            return emptyList();
        }
    }

    public EntityImplementationType(ResolvedTypeDeclaration typeName) {
        type = typeName;
    }
}
