package poussecafe.doc.process;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.process.DomainProcess;

public class AggregateDocCreation extends DomainProcess {

    public void addAggregateDoc(BoundedContextDocKey boundedContextKey, ClassDoc classDoc) {
        AggregateDoc aggregateDoc = aggregateDocFactory.newAggregateDoc(boundedContextKey, classDoc);
        runInTransaction(BoundedContextDoc.class, () -> aggregateDocRepository.add(aggregateDoc));
    }

    private AggregateDocFactory aggregateDocFactory;

    private AggregateDocRepository aggregateDocRepository;
}
