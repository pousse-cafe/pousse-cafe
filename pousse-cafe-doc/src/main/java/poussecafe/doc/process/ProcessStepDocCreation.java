package poussecafe.doc.process;

import com.sun.javadoc.ClassDoc;
import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.processstepdoc.ProcessStepDocExtractor;
import poussecafe.doc.model.processstepdoc.ProcessStepDocRepository;
import poussecafe.doc.model.processstepdoc.ProcessStepDoc;
import poussecafe.process.DomainProcess;

public class ProcessStepDocCreation extends DomainProcess {

    public void addProcessStepDoc(BoundedContextDocKey boundedContextKey, ClassDoc classDoc) {
        List<ProcessStepDoc> docs = processStepDocExtractor.extractProcessStepDocs(boundedContextKey, classDoc);
        for(ProcessStepDoc doc : docs) {
            runInTransaction(BoundedContextDoc.class, () -> processStepRepository.add(doc));
        }
    }

    private ProcessStepDocExtractor processStepDocExtractor;

    private ProcessStepDocRepository processStepRepository;
}
