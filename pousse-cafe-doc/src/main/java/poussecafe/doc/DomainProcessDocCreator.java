package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.process.DomainProcessDocCreation;

public class DomainProcessDocCreator extends BoundedContextComponentDocCreator {

    public DomainProcessDocCreator(RootDocWrapper rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(ClassDoc classDoc) {
        return DomainProcessDocFactory.isDomainProcessDoc(classDoc);
    }

    @Override
    protected String componentName() {
        return "domain process";
    }

    @Override
    protected void addDoc(BoundedContextDocKey boundedContextDocKey,
            ClassDoc componentClassDoc) {
        domainProcessDocCreation.addDomainProcessDoc(boundedContextDocKey, componentClassDoc);
    }

    private DomainProcessDocCreation domainProcessDocCreation;
}
