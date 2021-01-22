package poussecafe.source.analysis;

import org.eclipse.jdt.core.dom.TypeDeclaration;

public class AggregateContainerClass {

    public static boolean isAggregateContainerClass(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        AnnotatedElement<TypeDeclaration> annotatedElement = resolvedTypeDeclaration.asAnnotatedElement();
        return annotatedElement.findAnnotation(CompilationUnitResolver.AGGREGATE_ANNOTATION_CLASS).isPresent();
    }

    public AggregateContainerClass(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        this.resolvedTypeDeclaration = resolvedTypeDeclaration;
    }

    private ResolvedTypeDeclaration resolvedTypeDeclaration;

    public String aggregateName() {
        return resolvedTypeDeclaration.className().simple();
    }
}
