package poussecafe.doc.model.relation;

import poussecafe.domain.Factory;

public class RelationFactory extends Factory<RelationKey, Relation, Relation.Attributes> {

    public Relation newRelation(NewRelationParameters parameters) {
        Relation relation = newAggregateWithKey(new RelationKey(parameters.fromComponent.className(), parameters.toComponent.className()));
        relation.fromType(parameters.fromComponent.type());
        relation.toType(parameters.toComponent.type());
        return relation;
    }

    public static class NewRelationParameters {

        public Component fromComponent;

        public Component toComponent;
    }
}
