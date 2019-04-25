package poussecafe.doc.process;

import javax.lang.model.element.TypeElement;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.factorydoc.FactoryDoc;
import poussecafe.doc.model.factorydoc.FactoryDocFactory;
import poussecafe.doc.model.factorydoc.FactoryDocRepository;
import poussecafe.process.DomainProcess;

public class FactoryDocCreation extends DomainProcess {

    public void addFactoryDoc(BoundedContextDocId boundedContextId, TypeElement classDoc) {
        FactoryDoc aggregateDoc = aggregateDocFactory.newFactoryDoc(boundedContextId, classDoc);
        runInTransaction(BoundedContextDoc.class, () -> aggregateDocRepository.add(aggregateDoc));
    }

    private FactoryDocFactory aggregateDocFactory;

    private FactoryDocRepository aggregateDocRepository;
}
