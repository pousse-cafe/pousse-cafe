package poussecafe.doc.model.aggregatedoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;

public class AggregateDocFactory extends Factory<AggregateDocKey, AggregateDoc, AggregateDoc.Data> {

    public AggregateDoc newAggregateDoc(ClassDoc classDoc) {
        if(!isAggregateDoc(classDoc)) {
            throw new DomainException("Class " + classDoc.name() + " is not an aggregate root");
        }

        BoundedContextDoc boundedContextDoc = boundedContextDocRepository.findByPackageNamePrefixing(classDoc.containingPackage().name());
        if(boundedContextDoc == null) {
            throw new DomainException("Unable to locate bounded context for package " + classDoc.containingPackage().name());
        }

        String name = name(classDoc);
        AggregateDocKey key = new AggregateDocKey(boundedContextDoc.name(), name);
        AggregateDoc aggregateDoc = newStorableWithKey(key);
        aggregateDoc.description(classDoc.commentText());
        return aggregateDoc;
    }

    public static String name(ClassDoc classDoc) {
        return classDoc.simpleTypeName();
    }

    public static boolean isAggregateDoc(ClassDoc classDoc) {
        return ClassDocPredicates.documentsWithSuperclass(classDoc, AggregateRoot.class);
    }

    private BoundedContextDocRepository boundedContextDocRepository;
}
