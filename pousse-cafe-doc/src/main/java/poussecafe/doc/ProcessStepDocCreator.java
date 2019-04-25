package poussecafe.doc;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.model.factorydoc.FactoryDocFactory;
import poussecafe.doc.process.ProcessStepDocCreation;
import poussecafe.domain.Repository;

public class ProcessStepDocCreator extends BoundedContextComponentDocCreator {

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
    protected void addDoc(BoundedContextDocId boundedContextDocId,
            TypeElement componentClassDoc) {
        processStepDocCreation.createOrUpdateProcessStepDoc(boundedContextDocId, componentClassDoc);
    }

    private ProcessStepDocCreation processStepDocCreation;
}
