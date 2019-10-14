package poussecafe.doc.model.aggregatedoc.adapters;

import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.doc.model.aggregatedoc.AggregateDocId;

public class AggregateDocIdDataAdapter implements DataAdapter<String, AggregateDocId> {

    @Override
    public AggregateDocId adaptGet(String storedValue) {
        return AggregateDocId.ofClassName(storedValue);
    }

    @Override
    public String adaptSet(AggregateDocId valueToStore) {
        return valueToStore.stringValue();
    }

    public static AggregateDocIdDataAdapter instance() {
        return SINGLETON;
    }

    private static final AggregateDocIdDataAdapter SINGLETON = new AggregateDocIdDataAdapter();

    private AggregateDocIdDataAdapter() {

    }
}
