package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.model.factorydoc.FactoryDocFactory;
import poussecafe.doc.process.DomainProcessDocCreation;
import poussecafe.domain.Repository;

public class DomainProcessDocCreator extends BoundedContextComponentDocCreator {

    public DomainProcessDocCreator(RootDocWrapper rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(ClassDoc classDoc) {
        return DomainProcessDocFactory.isDomainProcessDoc(classDoc) ||
                AggregateDocFactory.isAggregateDoc(classDoc) ||
                FactoryDocFactory.isFactoryDoc(classDoc) ||
                ClassDocPredicates.documentsWithSuperclass(classDoc, Repository.class);
    }

    @Override
    protected String componentName() {
        return "domain process";
    }

    @Override
    protected void addDoc(BoundedContextDocKey boundedContextDocKey,
            ClassDoc componentClassDoc) {
        if(DomainProcessDocFactory.isDomainProcessDoc(componentClassDoc)) {
            domainProcessDocCreation.addDomainProcessDoc(boundedContextDocKey, componentClassDoc);
        } else {
            for(MethodDoc doc : componentClassDoc.methods()) {
                domainProcessDocCreation.addDomainProcessDocs(boundedContextDocKey, doc);
            }
        }
    }

    private DomainProcessDocCreation domainProcessDocCreation;
}
