package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.model.factorydoc.FactoryDocFactory;
import poussecafe.doc.process.ProcessStepDocCreation;
import poussecafe.domain.Repository;

public class ProcessStepDocCreator extends BoundedContextComponentDocCreator {

    public ProcessStepDocCreator(RootDocWrapper rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(ClassDoc classDoc) {
        return AggregateDocFactory.isAggregateDoc(classDoc) ||
                FactoryDocFactory.isFactoryDoc(classDoc) ||
                DomainProcessDocFactory.isDomainProcessDoc(classDoc) ||
                ClassDocPredicates.documentsWithSuperclass(classDoc, Repository.class);
    }

    @Override
    protected String componentName() {
        return "message listener container";
    }

    @Override
    protected void addDoc(BoundedContextDocKey boundedContextDocKey,
            ClassDoc componentClassDoc) {
        processStepDocCreation.createOrUpdateProcessStepDoc(boundedContextDocKey, componentClassDoc);
    }

    private ProcessStepDocCreation processStepDocCreation;
}
