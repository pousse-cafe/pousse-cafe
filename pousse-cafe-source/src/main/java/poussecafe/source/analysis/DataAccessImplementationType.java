package poussecafe.source.analysis;

import java.util.Optional;

public class DataAccessImplementationType {

    public static boolean isDataAccessImplementation(ResolvedTypeDeclaration type) {
        return type.name().instanceOf(CompilationUnitResolver.DATA_ACCESS_INTERFACE)
                && isConcreteImplementation(type);
    }

    private static boolean isConcreteImplementation(ResolvedTypeDeclaration type) {
        return !type.modifiers().isAbstract()
                && !type.typeDeclaration().isInterface();
    }

    public Optional<ResolvedTypeName> aggregateRoot() {
        var annotation = type.asAnnotatedElement().findAnnotation(
                CompilationUnitResolver.DATA_ACCESS_IMPLEMENTATION_ANNOTATION_CLASS);
        return annotation.map(a -> a.attribute("aggregateRoot").orElseThrow().asType());
    }

    private ResolvedTypeDeclaration type;

    public Optional<ResolvedTypeName> dataImplementation() {
        var annotation = type.asAnnotatedElement().findAnnotation(
                CompilationUnitResolver.DATA_ACCESS_IMPLEMENTATION_ANNOTATION_CLASS);
        return annotation.map(a -> a.attribute("dataImplementation").orElseThrow().asType());
    }

    public String storageName() {
        var annotation = type.asAnnotatedElement().findAnnotation(
                CompilationUnitResolver.DATA_ACCESS_IMPLEMENTATION_ANNOTATION_CLASS).orElseThrow();
        var value = annotation.attribute("storageName").orElseThrow();
        if(value.isStringLiteral()) {
            return value.asString();
        } else if(value.isQualifiedName()) {
            var constantValue = value.resolvedConstantValue();
            if(constantValue instanceof String) {
                return (String) constantValue;
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public DataAccessImplementationType(ResolvedTypeDeclaration typeName) {
        type = typeName;
    }
}
