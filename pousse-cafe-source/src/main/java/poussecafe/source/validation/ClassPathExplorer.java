package poussecafe.source.validation;

import java.util.Set;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.analysis.ResolvedClass;

public interface ClassPathExplorer {

    Set<ResolvedClass> getSubTypesOf(ClassName superTypeName);
}
