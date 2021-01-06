package poussecafe.source.analysis;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class RunnerClass {

    public static boolean isRunner(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        return resolvedTypeDeclaration.name().instanceOf(CompilationUnitResolver.AGGREGATE_MESSAGE_LISTENER_RUNNER_INTERFACE)
                && resolvedTypeDeclaration.isConcrete();
    }

    public RunnerClass(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        type = resolvedTypeDeclaration;
    }

    private ResolvedTypeDeclaration type;

    public Set<String> typeParametersQualifiedNames() {
        var names = new HashSet<String>();

        var superclass = type.superclassType();
        if(superclass.isPresent()) {
            names.addAll(typeParameters(superclass.get()));
        }

        var interfaces = type.superInterfaceTypes();
        for(ResolvedType interfaceObject : interfaces) {
            names.addAll(typeParameters(interfaceObject));
        }

        return names;
    }

    private List<String> typeParameters(ResolvedType type) {
        if(type.isParametrized()) {
            return type.typeParameters().stream()
                    .filter(ResolvedType::isSimpleType)
                    .map(ResolvedType::toTypeName)
                    .map(ResolvedTypeName::qualifiedName)
                    .collect(toList());
        } else {
            return Collections.emptyList();
        }
    }
}
