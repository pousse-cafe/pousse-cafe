package poussecafe.source.analysis;

public interface Resolver {

    ResolvedTypeName resolve(Name name);

    ClassResolver classResolver();
}
