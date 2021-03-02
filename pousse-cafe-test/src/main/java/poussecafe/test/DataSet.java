package poussecafe.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

public class DataSet {

    @SuppressWarnings("rawtypes")
    public Map<Class, List<EntityAttributes>> data() {
        return Collections.unmodifiableMap(data);
    }

    @SuppressWarnings("rawtypes")
    private Map<Class, List<EntityAttributes>> data = new HashMap<>();

    public static class Builder {

        public DataSet build() {
            return precondition;
        }

        private DataSet precondition = new DataSet();

        public <K, D extends EntityAttributes<K>, A extends AggregateRoot<K, D>>
        Builder withAggregateData(Class<A> aggregageRootClass, D data) {
            var list = precondition.data.computeIfAbsent(aggregageRootClass, key -> new ArrayList<>());
            list.add(data);
            return this;
        }
    }

    private DataSet() {

    }
}
