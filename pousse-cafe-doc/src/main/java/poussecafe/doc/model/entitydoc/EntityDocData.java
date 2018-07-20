package poussecafe.doc.model.entitydoc;

import java.io.Serializable;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;
import poussecafe.storable.Property;

@SuppressWarnings("serial")
public class EntityDocData implements EntityDoc.Data, Serializable {

    @Override
    public Property<EntityDocKey> key() {
        return new Property<EntityDocKey>() {
            @Override
            public EntityDocKey get() {
                return new EntityDocKey(new AggregateDocKey(boundedContextKey, aggretateName), entityName);
            }

            @Override
            public void set(EntityDocKey value) {
                boundedContextKey = value.aggregateDocKey().boundedContextKey();
                aggretateName = value.aggregateDocKey().name();
                entityName = value.name();
            }
        };
    }

    private String boundedContextKey;

    private String aggretateName;

    private String entityName;

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
