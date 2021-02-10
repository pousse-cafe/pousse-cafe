package poussecafe.source.analysis;

import java.util.List;
import java.util.Optional;
import poussecafe.source.Source;

public interface ResolvedClass {

    ClassName name();

    List<ResolvedClass> innerClasses();

    boolean instanceOf(String supertype) throws ClassNotFoundException;

    Optional<ResolvedClass> declaringClass();

    ClassResolver resolver();

    Optional<Object> staticFieldValue(String constantName);

    Source source();

    boolean isInterface();
}
