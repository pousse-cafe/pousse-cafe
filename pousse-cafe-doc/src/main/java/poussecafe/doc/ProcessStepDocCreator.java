package poussecafe.doc;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.process.ProcessStepDocCreation;

public class ProcessStepDocCreator extends ModuleComponentDocCreator {

    public ProcessStepDocCreator(DocletEnvironment rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(TypeElement classDoc) {
        return aggregateDocFactory.extendsAggregateRoot(classDoc) ||
                aggregateDocFactory.isFactoryDoc(classDoc) ||
                domainProcessDocFactory.isDomainProcessDoc(classDoc) ||
                aggregateDocFactory.isRepositoryDoc(classDoc);
    }

    private AggregateDocFactory aggregateDocFactory;

    private DomainProcessDocFactory domainProcessDocFactory;

    @Override
    protected String componentName() {
        return "message listener container";
    }

    @Override
    protected void addDoc(ModuleDocId moduleDocId,
            TypeElement componentClassDoc) {
        processStepDocCreation.createOrUpdateProcessStepDoc(moduleDocId, componentClassDoc);
    }

    private ProcessStepDocCreation processStepDocCreation;
}
