package poussecafe.doc.model.relation;

import poussecafe.domain.AggregateRoot;
import poussecafe.storable.IdentifiedStorableData;
import poussecafe.storable.Property;

public class Relation extends AggregateRoot<RelationKey, Relation.Data> {

    void fromType(ComponentType fromType) {
        getData().fromType().set(fromType);
    }

    void toType(ComponentType toType) {
        getData().toType().set(toType);
    }

    public Component fromComponent() {
        return new Component(getData().fromType().get(), getKey().fromClass());
    }

    public Component toComponent() {
        return new Component(getData().toType().get(), getKey().toClass());
    }

    public static interface Data extends IdentifiedStorableData<RelationKey> {

        Property<ComponentType> fromType();

        Property<ComponentType> toType();
    }
}
