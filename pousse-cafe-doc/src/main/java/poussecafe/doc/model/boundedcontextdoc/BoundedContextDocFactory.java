package poussecafe.doc.model.boundedcontextdoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.context.BoundedContext;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;

public class BoundedContextDocFactory extends Factory<String, BoundedContextDoc, BoundedContextDoc.Data> {

    public BoundedContextDoc newBoundedContextDoc(ClassDoc classDoc) {
        if(!isBoundedContextDoc(classDoc)) {
            throw new DomainException("Class " + classDoc.name() + " is not a valid bounded context");
        }

        String name = classDoc.simpleTypeName();
        BoundedContextDoc boundedContextDoc = newAggregateWithKey(name);
        boundedContextDoc.description(classDoc.commentText());
        boundedContextDoc.packageName(classDoc.containingPackage().name());
        return boundedContextDoc;
    }

    public static boolean isBoundedContextDoc(ClassDoc classDoc) {
        return classDoc.superclass() != null && classDoc.superclass().qualifiedName().equals(BoundedContext.class.getName());
    }

}
