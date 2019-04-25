package poussecafe.doc;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.DocletAccess;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.model.factorydoc.FactoryDocFactory;
import poussecafe.doc.process.DomainProcessDocCreation;
import poussecafe.domain.Repository;

public class DomainProcessDocCreator extends BoundedContextComponentDocCreator {

    public DomainProcessDocCreator(DocletEnvironment rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(TypeElement classDoc) {
        return domainProcessDocFactory.isDomainProcessDoc(classDoc) ||
                aggregateDocFactory.isAggregateDoc(classDoc) ||
                factoryDocFactory.isFactoryDoc(classDoc) ||
                classDocPredicates.documentsWithSuperclass(classDoc, Repository.class);
    }

    private DomainProcessDocFactory domainProcessDocFactory;

    private AggregateDocFactory aggregateDocFactory;

    private FactoryDocFactory factoryDocFactory;

    private ClassDocPredicates classDocPredicates;

    @Override
    protected String componentName() {
        return "domain process";
    }

    @Override
    protected void addDoc(BoundedContextDocId boundedContextDocId,
            TypeElement componentClassDoc) {
        if(domainProcessDocFactory.isDomainProcessDoc(componentClassDoc)) {
            domainProcessDocCreation.addDomainProcessDoc(boundedContextDocId, componentClassDoc);
        } else {
            for(ExecutableElement doc : docletAccess.methods(componentClassDoc)) {
                if(domainProcessDocFactory.isDomainProcessDoc(doc)) {
                    domainProcessDocCreation.addDomainProcessDocs(boundedContextDocId, doc);
                }
            }
        }
    }

    private DomainProcessDocCreation domainProcessDocCreation;

    private DocletAccess docletAccess;
}
