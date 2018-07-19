package poussecafe.doc.model.aggregatedoc;

import java.io.Serializable;
import poussecafe.storable.Property;

@SuppressWarnings("serial")
public class AggregateDocData implements AggregateDoc.Data, Serializable {

    @Override
    public Property<AggregateDocKey> key() {
        return new Property<AggregateDocKey>() {
            @Override
            public AggregateDocKey get() {
                return new AggregateDocKey(boundedContextKey, name);
            }

            @Override
            public void set(AggregateDocKey value) {
                boundedContextKey = value.boundedContextKey();
                name = value.name();
            }
        };
    }

    private String boundedContextKey;

    private String name;

    @Override
    public Property<String> description() {
        return new Property<String>() {
            @Override
            public String get() {
                return description;
            }

            @Override
            public void set(String value) {
                description = value;
            }
        };
    }

    private String description;
}
