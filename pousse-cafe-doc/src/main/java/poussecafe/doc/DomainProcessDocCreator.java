package poussecafe.doc;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.DocletAccess;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.process.DomainProcessDocCreation;

public class DomainProcessDocCreator extends ModuleComponentDocCreator {

    public DomainProcessDocCreator(DocletEnvironment rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(TypeElement classDoc) {
        return domainProcessDocFactory.isDomainProcessDoc(classDoc) ||
                aggregateDocFactory.isAggregateDoc(classDoc) ||
                aggregateDocFactory.isFactoryDoc(classDoc) ||
                aggregateDocFactory.isRepositoryDoc(classDoc);
    }

    private DomainProcessDocFactory domainProcessDocFactory;

    private AggregateDocFactory aggregateDocFactory;

    @Override
    protected String componentName() {
        return "domain process";
    }

    @Override
    protected void addDoc(ModuleDocId moduleDocId,
            TypeElement componentClassDoc) {
        if(domainProcessDocFactory.isDomainProcessDoc(componentClassDoc)) {
            domainProcessDocCreation.addDomainProcessDoc(moduleDocId, componentClassDoc);
        } else {
            for(ExecutableElement doc : docletAccess.methods(componentClassDoc)) {
                if(domainProcessDocFactory.isDomainProcessDoc(doc)) {
                    domainProcessDocCreation.addDomainProcessDocs(moduleDocId, doc);
                }
            }
        }
    }

    private DomainProcessDocCreation domainProcessDocCreation;

    private DocletAccess docletAccess;
}
