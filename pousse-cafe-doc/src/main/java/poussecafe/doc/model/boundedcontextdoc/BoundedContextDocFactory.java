package poussecafe.doc.model.boundedcontextdoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.context.BoundedContext;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;

public class BoundedContextDocFactory extends Factory<BoundedContextDocKey, BoundedContextDoc, BoundedContextDoc.Data> {

    public BoundedContextDoc newBoundedContextDoc(ClassDoc classDoc) {
        if(!isBoundedContextDoc(classDoc)) {
            throw new DomainException("Class " + classDoc.name() + " is not a valid bounded context");
        }

        String name = classDoc.simpleTypeName();
        BoundedContextDoc boundedContextDoc = newAggregateWithKey(BoundedContextDocKey.ofClassName(classDoc.qualifiedTypeName()));
        boundedContextDoc.componentDoc(new ComponentDoc.Builder()
                .name(name)
                .description(classDoc.commentText())
                .build());
        boundedContextDoc.packageName(classDoc.containingPackage().name());
        return boundedContextDoc;
    }

    public static boolean isBoundedContextDoc(ClassDoc classDoc) {
        return ClassDocPredicates.documentsWithSuperclass(classDoc, BoundedContext.class);
    }

}
