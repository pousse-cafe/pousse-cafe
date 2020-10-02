package poussecafe.doc.model.relation;

import poussecafe.domain.AggregateFactory;

public class RelationFactory extends AggregateFactory<RelationId, Relation, Relation.Attributes> {

    public Relation newRelation(NewRelationParameters parameters) {
        Relation relation = newAggregateWithId(new RelationId(parameters.fromComponent.className(), parameters.toComponent.className()));
        relation.fromType(parameters.fromComponent.type());
        relation.toType(parameters.toComponent.type());
        return relation;
    }

    public static class NewRelationParameters {

        public Component fromComponent;

        public Component toComponent;
    }
}
