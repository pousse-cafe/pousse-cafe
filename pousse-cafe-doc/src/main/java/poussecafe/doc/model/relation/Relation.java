package poussecafe.doc.model.relation;

import poussecafe.contextconfigurer.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.Property;

@Aggregate(
  factory = RelationFactory.class,
  repository = RelationRepository.class
)
public class Relation extends AggregateRoot<RelationKey, Relation.Data> {

    void fromType(ComponentType fromType) {
        data().fromType().set(fromType);
    }

    void toType(ComponentType toType) {
        data().toType().set(toType);
    }

    public Component fromComponent() {
        return new Component(data().fromType().get(), data().key().get().fromClass());
    }

    public Component toComponent() {
        return new Component(data().toType().get(), data().key().get().toClass());
    }

    public static interface Data extends EntityData<RelationKey> {

        Property<ComponentType> fromType();

        Property<ComponentType> toType();
    }
}
