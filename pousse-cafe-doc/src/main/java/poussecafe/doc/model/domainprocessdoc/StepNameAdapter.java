package poussecafe.doc.model.domainprocessdoc;

import poussecafe.attribute.adapters.DataAdapter;

public class StepNameAdapter implements DataAdapter<String, StepName> {

    @Override
    public StepName adaptGet(String storedValue) {
        return new StepName(storedValue);
    }

    @Override
    public String adaptSet(StepName valueToStore) {
        return valueToStore.stringValue();
    }

    public static StepNameAdapter instance() {
        return INSTANCE;
    }

    private static final StepNameAdapter INSTANCE = new StepNameAdapter();
}
