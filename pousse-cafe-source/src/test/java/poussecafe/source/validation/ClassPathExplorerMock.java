package poussecafe.source.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import poussecafe.source.analysis.Name;
import poussecafe.source.analysis.ResolvedClass;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClassPathExplorerMock implements ClassPathExplorer {

    @Override
    public Set<ResolvedClass> getSubTypesOf(Name superTypeName) {
        return getOrCreate(superTypeName);
    }

    private Set<ResolvedClass> getOrCreate(Name superTypeName) {
        return subtypes.computeIfAbsent(superTypeName, key -> new HashSet<>());
    }

    private Map<Name, Set<ResolvedClass>> subtypes = new HashMap<>();

    public void addSubType(Name superTypeName, Name subtype) {
        var set = getOrCreate(superTypeName);
        set.add(mockResolvedClass(subtype));
    }

    private ResolvedClass mockResolvedClass(Name subtype) {
        var resolvedClass = mock(ResolvedClass.class);
        when(resolvedClass.name()).thenReturn(subtype);
        return resolvedClass;
    }
}
