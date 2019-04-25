package poussecafe.doc.process;

import java.util.List;
import java.util.Optional;
import javax.lang.model.element.TypeElement;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.processstepdoc.ProcessStepDoc;
import poussecafe.doc.model.processstepdoc.ProcessStepDocExtractor;
import poussecafe.doc.model.processstepdoc.ProcessStepDocId;
import poussecafe.doc.model.processstepdoc.ProcessStepDocRepository;
import poussecafe.process.DomainProcess;

public class ProcessStepDocCreation extends DomainProcess {

    public void createOrUpdateProcessStepDoc(BoundedContextDocId boundedContextId, TypeElement classDoc) {
        List<ProcessStepDoc> docs = processStepDocExtractor.extractProcessStepDocs(boundedContextId, classDoc);
        for(ProcessStepDoc doc : docs) {
            createOrUpdate(doc);
        }
    }

    private ProcessStepDocExtractor processStepDocExtractor;

    private void createOrUpdate(ProcessStepDoc doc) {
        ProcessStepDocId id = doc.attributes().identifier().value();
        if(processStepRepository.find(id) == null) {
            runInTransaction(BoundedContextDoc.class, () -> processStepRepository.add(doc));
        } else {
            runInTransaction(BoundedContextDoc.class, () -> {
                ProcessStepDoc processStepDoc = processStepRepository.get(id);
                Optional<String> processName = processStepDoc.attributes().processName().value();
                if(!processName.isPresent()) {
                    processStepDoc.attributes().processName().valueOf(doc.attributes().processName());
                }
                processStepDoc.attributes().producedEvents().addAll(doc.attributes().producedEvents().value());
                processStepDoc.attributes().fromExternals().addAll(doc.attributes().fromExternals().value());
                processStepDoc.attributes().toExternals().addAll(doc.attributes().toExternals().value());
                processStepRepository.update(processStepDoc);
            });
        }
    }

    private ProcessStepDocRepository processStepRepository;
}
