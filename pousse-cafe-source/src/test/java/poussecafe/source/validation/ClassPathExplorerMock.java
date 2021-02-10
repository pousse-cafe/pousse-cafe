package poussecafe.source.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.analysis.ResolvedClass;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClassPathExplorerMock implements ClassPathExplorer {

    @Override
    public Set<ResolvedClass> getSubTypesOf(ClassName superTypeName) {
        return getOrCreate(superTypeName);
    }

    private Set<ResolvedClass> getOrCreate(ClassName superTypeName) {
        return subtypes.computeIfAbsent(superTypeName, key -> new HashSet<>());
    }

    private Map<ClassName, Set<ResolvedClass>> subtypes = new HashMap<>();

    public void addSubType(ClassName superTypeName, ClassName subtype) {
        var set = getOrCreate(superTypeName);
        set.add(mockResolvedClass(subtype));
    }

    private ResolvedClass mockResolvedClass(ClassName subtype) {
        var resolvedClass = mock(ResolvedClass.class);
        when(resolvedClass.name()).thenReturn(subtype);
        return resolvedClass;
    }
}
