package poussecafe.doc.model;

import com.sun.javadoc.PackageDoc;
import org.apache.commons.lang3.StringUtils;
import poussecafe.doc.DDDAnnotationsResolver;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;

public class BoundedContextDocFactory extends Factory<String, BoundedContextDoc, BoundedContextDoc.Data> {

    public BoundedContextDoc newBoundedContextDoc(PackageDoc packageDoc) {
        if(!isBoundedContextDoc(packageDoc)) {
            throw new DomainException("Package " + packageDoc.name() + " is not a valid bounded context");
        }

        String boundedContextKey = getBoundedContextKey(packageDoc);
        BoundedContextDoc boundedContextDoc = newAggregateWithKey(boundedContextKey);
        boundedContextDoc.name(boundedContextKey);
        boundedContextDoc.description(packageDoc.commentText());
        boundedContextDoc.packageName(packageDoc.name());
        return boundedContextDoc;
    }

    public static boolean isBoundedContextDoc(PackageDoc packageDoc) {
        return !StringUtils.isEmpty(getBoundedContextKey(packageDoc));
    }

    private static String getBoundedContextKey(PackageDoc packageDoc) {
        return DDDAnnotationsResolver.getBoundedContext(packageDoc);
    }

}
