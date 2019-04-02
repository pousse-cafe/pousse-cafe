package poussecafe.doc.model.relation;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = RelationFactory.class,
  repository = RelationRepository.class
)
public class Relation extends AggregateRoot<RelationId, Relation.Attributes> {

    void fromType(ComponentType fromType) {
        attributes().fromType().value(fromType);
    }

    void toType(ComponentType toType) {
        attributes().toType().value(toType);
    }

    public Component fromComponent() {
        return new Component(attributes().fromType().value(), attributes().identifier().value().fromClass());
    }

    public Component toComponent() {
        return new Component(attributes().toType().value(), attributes().identifier().value().toClass());
    }

    public static interface Attributes extends EntityAttributes<RelationId> {

        Attribute<ComponentType> fromType();

        Attribute<ComponentType> toType();
    }
}
