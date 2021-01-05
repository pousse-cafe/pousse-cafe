package poussecafe.source.validation;

import java.util.Set;
import poussecafe.source.analysis.Name;
import poussecafe.source.analysis.ResolvedClass;

public interface ClassPathExplorer {

    Set<ResolvedClass> getSubTypesOf(Name superTypeName);
}
