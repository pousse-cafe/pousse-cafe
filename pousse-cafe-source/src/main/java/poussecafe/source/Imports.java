package poussecafe.source;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Name;

public class Imports {

    public boolean tryRegister(ImportDeclaration importDeclaration, Class<?> expectedClass) {
        Name importName = importDeclaration.getName();
        if(!imports.contains(expectedClass)
                && importName.isQualifiedName()
                && importName.getFullyQualifiedName().equals(expectedClass.getCanonicalName())) {
            imports.add(expectedClass);
            return true;
        }
        return false;
    }

    private Set<Class<?>> imports = new HashSet<>();

    public boolean hasImport(Class<?> expectedClass) {
        return imports.contains(expectedClass);
    }

    public ResolvedTypeName resolve(Name name) {
        return new ResolvedTypeName.Builder()
                .withImports(this)
                .withName(name)
                .build();
    }
}
