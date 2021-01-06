package poussecafe.source.analysis;

import org.eclipse.jdt.core.dom.SimpleType;

public interface Resolver {

    ResolvedTypeName resolve(Name name);

    ClassResolver classResolver();

    default ResolvedTypeName resolve(SimpleType simpleType) {
        return resolve(new Name(simpleType.getName()));
    }
}
