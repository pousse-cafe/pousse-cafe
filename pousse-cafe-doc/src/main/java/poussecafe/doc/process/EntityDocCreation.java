package poussecafe.doc.process;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.process.DomainProcess;

public class EntityDocCreation extends DomainProcess {

    public void addEntityDoc(AggregateDocKey aggregateDocKey, ClassDoc entityClassDoc) {
        EntityDoc entityDoc = entityDocFactory.newEntityDoc(aggregateDocKey, entityClassDoc);
        runInTransaction(EntityDoc.class, () -> entityDocRepository.add(entityDoc));
    }

    private EntityDocFactory entityDocFactory;

    private EntityDocRepository entityDocRepository;
}
