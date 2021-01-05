package poussecafe.source.analysis;

public class ProcessDefinitionType {

    public static boolean isProcessDefinition(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        return resolvedTypeDeclaration.implementsInterface(CompilationUnitResolver.PROCESS_INTERFACE);
    }

    public ProcessDefinitionType(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        if(!isProcessDefinition(resolvedTypeDeclaration)) {
            throw new IllegalArgumentException();
        }
        this.resolvedTypeDeclaration = resolvedTypeDeclaration;
    }

    private ResolvedTypeDeclaration resolvedTypeDeclaration;

    public String processName() {
        return resolvedTypeDeclaration.name().simpleName();
    }

    public Name className() {
        return resolvedTypeDeclaration.className();
    }
}
