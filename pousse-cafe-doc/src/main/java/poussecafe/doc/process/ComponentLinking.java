package poussecafe.doc.process;

import poussecafe.doc.model.relation.Relation;
import poussecafe.doc.model.relation.RelationFactory;
import poussecafe.doc.model.relation.RelationFactory.NewRelationParameters;
import poussecafe.doc.model.relation.RelationRepository;
import poussecafe.process.DomainProcess;

public class ComponentLinking extends DomainProcess {

    public void linkComponents(NewRelationParameters parameters) {
        Relation relation = relationFactory.newRelation(parameters);
        if(relationRepository.getOptional(relation.attributes().identifier().value()).isEmpty()) {
            runInTransaction(Relation.class, () -> relationRepository.add(relation));
        }
    }

    private RelationFactory relationFactory;

    private RelationRepository relationRepository;
}
