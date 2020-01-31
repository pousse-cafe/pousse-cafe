package poussecafe.doc.process;

import java.util.List;
import java.util.Optional;
import javax.lang.model.element.TypeElement;
import poussecafe.doc.model.aggregatedoc.AggregateDocId;
import poussecafe.doc.model.moduledoc.ModuleDoc;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.model.processstepdoc.ProcessStepDoc;
import poussecafe.doc.model.processstepdoc.ProcessStepDocExtractor;
import poussecafe.doc.model.processstepdoc.ProcessStepDocId;
import poussecafe.doc.model.processstepdoc.ProcessStepDocRepository;
import poussecafe.process.DomainProcess;

public class ProcessStepDocCreation extends DomainProcess {

    public void createOrUpdateProcessStepDoc(ModuleDocId moduleDocId, TypeElement classDoc) {
        List<ProcessStepDoc> docs = processStepDocExtractor.extractProcessStepDocs(moduleDocId, classDoc);
        for(ProcessStepDoc doc : docs) {
            createOrUpdate(doc);
        }
    }

    private ProcessStepDocExtractor processStepDocExtractor;

    private void createOrUpdate(ProcessStepDoc doc) {
        ProcessStepDocId id = doc.attributes().identifier().value();
        if(processStepRepository.getOptional(id).isEmpty()) {
            runInTransaction(ModuleDoc.class, () -> processStepRepository.add(doc));
        } else {
            runInTransaction(ModuleDoc.class, () -> {
                ProcessStepDoc processStepDoc = processStepRepository.get(id);

                processStepDoc.attributes().processNames().addAll(doc.attributes().processNames().value());
                processStepDoc.attributes().producedEvents().addAll(doc.attributes().producedEvents().value());
                processStepDoc.attributes().fromExternals().addAll(doc.attributes().fromExternals().value());
                processStepDoc.attributes().toExternals().addAll(doc.attributes().toExternals().value());
                processStepDoc.attributes().toExternalsByEvent().putAll(doc.attributes().toExternalsByEvent().value());

                Optional<AggregateDocId> aggregate = doc.attributes().aggregate().value();
                if(aggregate.isPresent()) {
                    processStepDoc.attributes().aggregate().valueOf(doc.attributes().aggregate());
                }

                processStepRepository.update(processStepDoc);
            });
        }
    }

    private ProcessStepDocRepository processStepRepository;
}
