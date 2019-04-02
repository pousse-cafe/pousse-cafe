package poussecafe.doc.process;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.process.DomainProcess;

public class EntityDocCreation extends DomainProcess {

    public void addEntityDoc(BoundedContextDocId boundedContextId, ClassDoc entityClassDoc) {
        EntityDoc entityDoc = entityDocFactory.newEntityDoc(boundedContextId, entityClassDoc);
        runInTransaction(EntityDoc.class, () -> entityDocRepository.add(entityDoc));
    }

    private EntityDocFactory entityDocFactory;

    private EntityDocRepository entityDocRepository;
}
