package poussecafe.source.analysis;

import static java.util.Objects.requireNonNull;

public class ModuleClass {

    public static boolean isModule(ResolvedTypeDeclaration declaration) {
        return declaration.name().instanceOf(CompilationUnitResolver.MODULE_INTERFACE);
    }

    public ModuleClass(ResolvedTypeDeclaration declaration) {
        requireNonNull(declaration);
        this.declaration = declaration;
    }

    private ResolvedTypeDeclaration declaration;

    public ClassName className() {
        return new ClassName(declaration.name().qualifiedName());
    }

    public String basePackage() {
        return declaration.name().packageName();
    }
}
