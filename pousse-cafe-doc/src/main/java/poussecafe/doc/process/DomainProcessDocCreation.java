package poussecafe.doc.process;

import java.util.List;
import java.util.Optional;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocId;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocRepository;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.process.DomainProcess;

public class DomainProcessDocCreation extends DomainProcess {

    public void addDomainProcessDoc(ModuleDocId moduleDocId, TypeElement classDoc) {
        DomainProcessDoc entityDoc = domainProcessDocFactory.newDomainProcessDoc(moduleDocId, classDoc);
        runInTransaction(DomainProcessDoc.class, () -> domainProcessDocRepository.add(entityDoc));
    }

    private DomainProcessDocFactory domainProcessDocFactory;

    private DomainProcessDocRepository domainProcessDocRepository;

    public void addDomainProcessDocs(ModuleDocId moduleDocId, ExecutableElement classDoc) {
        List<DomainProcessDoc> docs = domainProcessDocFactory.createDomainProcesses(moduleDocId, classDoc);
        for(DomainProcessDoc doc : docs) {
            DomainProcessDocId docId = doc.attributes().identifier().value();
            ModuleComponentDoc moduleComponentDoc = doc.attributes().moduleComponentDoc().value();
            Optional<DomainProcessDoc> existingDoc = domainProcessDocRepository.getOptional(docId);
            if(existingDoc.isPresent()) {
                if(moduleComponentDoc.componentDoc().hasDescription()) {
                    runInTransaction(DomainProcessDoc.class, () -> {
                        DomainProcessDoc toUpdate = domainProcessDocRepository.get(docId);
                        toUpdate.attributes().moduleComponentDoc().value(moduleComponentDoc);
                        domainProcessDocRepository.update(toUpdate);
                    });
                }
            } else {
                runInTransaction(DomainProcessDoc.class, () -> domainProcessDocRepository.add(doc));
            }
        }
    }
}
