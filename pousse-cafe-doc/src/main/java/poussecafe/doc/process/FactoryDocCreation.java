package poussecafe.doc.process;

import javax.lang.model.element.TypeElement;
import poussecafe.doc.model.factorydoc.FactoryDoc;
import poussecafe.doc.model.factorydoc.FactoryDocFactory;
import poussecafe.doc.model.factorydoc.FactoryDocRepository;
import poussecafe.doc.model.moduledoc.ModuleDoc;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.process.DomainProcess;

public class FactoryDocCreation extends DomainProcess {

    public void addFactoryDoc(ModuleDocId moduleDocId, TypeElement classDoc) {
        FactoryDoc aggregateDoc = aggregateDocFactory.newFactoryDoc(moduleDocId, classDoc);
        runInTransaction(ModuleDoc.class, () -> aggregateDocRepository.add(aggregateDoc));
    }

    private FactoryDocFactory aggregateDocFactory;

    private FactoryDocRepository aggregateDocRepository;
}
