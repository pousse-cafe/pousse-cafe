package poussecafe.doc.model;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.domain.DomainException;
import poussecafe.domain.Service;

public class AggregateDocLocator implements Service {

    public AggregateDoc locateAggregateDoc(ClassDoc classDoc) {
        if(!AggregateDocFactory.isAggregateDoc(classDoc)) {
            throw new DomainException("Class " + classDoc.name() + " is not an aggregate root");
        }

        BoundedContextDoc boundedContextDoc = boundedContextDocRepository.findByPackageNamePrefixing(classDoc.containingPackage().name());
        if(boundedContextDoc == null) {
            return null;
        }

        return aggregateDocRepository.get(new AggregateDocKey(boundedContextDoc.getKey(), AggregateDocFactory.name(classDoc)));
    }

    private BoundedContextDocRepository boundedContextDocRepository;

    private AggregateDocRepository aggregateDocRepository;
}
