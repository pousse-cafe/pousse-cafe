package poussecafe.doc.process;

import com.sun.javadoc.ClassDoc;
import java.util.List;
import java.util.Optional;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.processstepdoc.ProcessStepDoc;
import poussecafe.doc.model.processstepdoc.ProcessStepDocExtractor;
import poussecafe.doc.model.processstepdoc.ProcessStepDocKey;
import poussecafe.doc.model.processstepdoc.ProcessStepDocRepository;
import poussecafe.process.DomainProcess;

public class ProcessStepDocCreation extends DomainProcess {

    public void createOrUpdateProcessStepDoc(BoundedContextDocKey boundedContextKey, ClassDoc classDoc) {
        List<ProcessStepDoc> docs = processStepDocExtractor.extractProcessStepDocs(boundedContextKey, classDoc);
        for(ProcessStepDoc doc : docs) {
            createOrUpdate(doc);
        }
    }

    private ProcessStepDocExtractor processStepDocExtractor;

    private void createOrUpdate(ProcessStepDoc doc) {
        ProcessStepDocKey key = doc.attributes().key().value();
        if(processStepRepository.find(key) == null) {
            runInTransaction(BoundedContextDoc.class, () -> processStepRepository.add(doc));
        } else {
            runInTransaction(BoundedContextDoc.class, () -> {
                ProcessStepDoc processStepDoc = processStepRepository.get(key);
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
