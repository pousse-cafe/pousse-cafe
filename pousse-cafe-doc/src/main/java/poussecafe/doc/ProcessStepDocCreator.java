package poussecafe.doc;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.model.factorydoc.FactoryDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.process.ProcessStepDocCreation;
import poussecafe.domain.Repository;

public class ProcessStepDocCreator extends ModuleComponentDocCreator {

    public ProcessStepDocCreator(DocletEnvironment rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(TypeElement classDoc) {
        return aggregateDocFactory.isAggregateDoc(classDoc) ||
                factoryDocFactory.isFactoryDoc(classDoc) ||
                domainProcessDocFactory.isDomainProcessDoc(classDoc) ||
                classDocPredicates.documentsWithSuperclass(classDoc, Repository.class);
    }

    private AggregateDocFactory aggregateDocFactory;

    private FactoryDocFactory factoryDocFactory;

    private DomainProcessDocFactory domainProcessDocFactory;

    private ClassDocPredicates classDocPredicates;

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
