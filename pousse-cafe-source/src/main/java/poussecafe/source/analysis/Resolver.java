package poussecafe.source.analysis;

import org.eclipse.jdt.core.dom.SimpleType;

public interface Resolver {

    ResolvedTypeName resolve(ClassName name);

    ClassResolver classResolver();

    default ResolvedTypeName resolve(SimpleType simpleType) {
        return resolve(new ClassName(simpleType.getName()));
    }
}
