package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.process.AggregateDocCreation;

public class AggregateDocCreator extends BoundedContextComponentDocCreator {

    public AggregateDocCreator(RootDocWrapper rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(ClassDoc classDoc) {
        return AggregateDocFactory.isAggregateDoc(classDoc);
    }

    @Override
    protected String componentName() {
        return "aggregate";
    }

    @Override
    protected void addDoc(BoundedContextDocKey boundedContextDocKey,
            ClassDoc componentClassDoc) {
        aggregateDocCreation.addAggregateDoc(boundedContextDocKey, componentClassDoc);
    }

    private AggregateDocCreation aggregateDocCreation;
}
