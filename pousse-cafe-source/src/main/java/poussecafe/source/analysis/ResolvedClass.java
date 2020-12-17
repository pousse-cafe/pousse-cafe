package poussecafe.source.analysis;

import java.util.List;
import java.util.Optional;

public interface ResolvedClass {

    Name name();

    List<ResolvedClass> innerClasses();

    boolean instanceOf(String supertype) throws ClassNotFoundException;

    Optional<ResolvedClass> declaringClass();

    ClassResolver resolver();
}
