package poussecafe.doc.process;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.factorydoc.FactoryDoc;
import poussecafe.doc.model.factorydoc.FactoryDocFactory;
import poussecafe.doc.model.factorydoc.FactoryDocRepository;
import poussecafe.process.DomainProcess;

public class FactoryDocCreation extends DomainProcess {

    public void addFactoryDoc(BoundedContextDocKey boundedContextKey, ClassDoc classDoc) {
        FactoryDoc aggregateDoc = aggregateDocFactory.newFactoryDoc(boundedContextKey, classDoc);
        runInTransaction(BoundedContextDoc.class, () -> aggregateDocRepository.add(aggregateDoc));
    }

    private FactoryDocFactory aggregateDocFactory;

    private FactoryDocRepository aggregateDocRepository;
}
