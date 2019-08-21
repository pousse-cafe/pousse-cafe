package poussecafe.doc.process;

import javax.lang.model.element.TypeElement;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.process.DomainProcess;

public class EntityDocCreation extends DomainProcess {

    public void addEntityDoc(ModuleDocId moduleDocId, TypeElement entityClassDoc) {
        EntityDoc entityDoc = entityDocFactory.newEntityDoc(moduleDocId, entityClassDoc);
        runInTransaction(EntityDoc.class, () -> entityDocRepository.add(entityDoc));
    }

    private EntityDocFactory entityDocFactory;

    private EntityDocRepository entityDocRepository;
}
