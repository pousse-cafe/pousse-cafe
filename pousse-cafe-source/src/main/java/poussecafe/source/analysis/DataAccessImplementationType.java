package poussecafe.source.analysis;

public class DataAccessImplementationType {

    public static boolean isDataAccessImplementation(ResolvedTypeDeclaration type) {
        return type.asAnnotatedElement().findAnnotation(
                CompilationUnitResolver.DATA_ACCESS_IMPLEMENTATION_ANNOTATION_CLASS).isPresent();
    }

    public ResolvedTypeName aggregateRoot() {
        var annotation = type.asAnnotatedElement().findAnnotation(
                CompilationUnitResolver.DATA_ACCESS_IMPLEMENTATION_ANNOTATION_CLASS).orElseThrow();
        return annotation.attribute("aggregateRoot").orElseThrow().asType();
    }

    private ResolvedTypeDeclaration type;

    public ResolvedTypeName dataImplementation() {
        var annotation = type.asAnnotatedElement().findAnnotation(
                CompilationUnitResolver.DATA_ACCESS_IMPLEMENTATION_ANNOTATION_CLASS).orElseThrow();
        return annotation.attribute("dataImplementation").orElseThrow().asType();
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
