package poussecafe.doc.model.boundedcontextdoc;

import com.sun.javadoc.PackageDoc;
import poussecafe.doc.AnnotationsResolver;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;

public class BoundedContextDocFactory extends Factory<BoundedContextDocKey, BoundedContextDoc, BoundedContextDoc.Data> {

    public BoundedContextDoc newBoundedContextDoc(PackageDoc packageDoc) {
        if(!isBoundedContextDoc(packageDoc)) {
            throw new DomainException("Package " + packageDoc.name() + " is not a valid bounded context");
        }

        String name = AnnotationsResolver.boundedContext(packageDoc);
        BoundedContextDoc boundedContextDoc = newAggregateWithKey(BoundedContextDocKey.ofPackageName(packageDoc.name()));
        boundedContextDoc.componentDoc(componentDocFactory.buildDoc(name, packageDoc));
        return boundedContextDoc;
    }

    private ComponentDocFactory componentDocFactory;

    public static boolean isBoundedContextDoc(PackageDoc packageDoc) {
        return AnnotationsResolver.isBoundedContext(packageDoc);
    }

}
