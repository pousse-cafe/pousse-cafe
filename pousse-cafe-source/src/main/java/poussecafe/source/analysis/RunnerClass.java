package poussecafe.source.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;

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
        var declaration = type.typeDeclaration();

        var superclass = declaration.getSuperclassType();
        names.addAll(typeParameters(superclass));

        var interfaces = declaration.superInterfaceTypes();
        for(Object interfaceObject : interfaces) {
            names.addAll(typeParameters(interfaceObject));
        }

        return names;
    }

    private List<String> typeParameters(Object type) {
        if(type instanceof ParameterizedType) {
            ParameterizedType superclassParameterizedType = (ParameterizedType) type;
            return typeParameters(superclassParameterizedType.typeArguments());
        } else {
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("rawtypes")
    private List<String> typeParameters(List typeParametersObjects) {
        if(typeParametersObjects.isEmpty()) {
            return Collections.emptyList();
        } else {
            var names = new ArrayList<String>();
            for(int i = 0; i < typeParametersObjects.size(); ++i) {
                var typeParameterObject = typeParametersObjects.get(i);
                if(typeParameterObject instanceof SimpleType) {
                    names.add(type.typeParameter(i).qualifiedName());
                }
            }
            return names;
        }
    }
}
